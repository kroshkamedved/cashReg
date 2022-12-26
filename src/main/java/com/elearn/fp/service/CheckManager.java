package com.elearn.fp.service;

import com.elearn.fp.db.entity.Item;
import com.elearn.fp.exception.DBException;
import com.elearn.fp.db.entity.Order;
import com.elearn.fp.db.entity.User;
import com.elearn.fp.db.entity.UserRole;
import com.elearn.fp.db.utils.DBManager;
import com.elearn.fp.db.utils.JdbcUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.elearn.fp.db.query.Query.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.*;
import java.util.*;
import java.util.Date;

public class CheckManager {
    private Logger logger = LogManager.getLogger(CheckManager.class);
    private static CheckManager instance;

    public static synchronized CheckManager getInstance() {
        if (instance == null) {
            instance = new CheckManager();
        }
        return instance;
    }

    DBManager dbManager;

    private CheckManager() {
        dbManager = DBManager.getInstance();
    }

    /**
     * confirm check and update database
     *
     * @param request as HttpServletRequest
     * @param goods   as HashMap(item,itemQuantity)
     * @throws DBException when cannot confirm check
     */
    public void confirmCheck(HttpServletRequest request, HashMap<Item, Integer> goods) throws DBException {
        Connection connection = null;
        PreparedStatement createNewOrder = null;
        PreparedStatement insertToOrderItems = null;
        long orderId = 0;
        try {
            connection = dbManager.getConnection();
            connection.setAutoCommit(false);
            createNewOrder = connection.prepareStatement(INSERT_INTO_ORDERS, Statement.RETURN_GENERATED_KEYS);
            Date date = new Date();
            Object param = new java.sql.Timestamp(date.getTime());
            createNewOrder.setObject(1, param);
            User usr = (User) request.getSession().getAttribute("usr");
            createNewOrder.setInt(2, usr.getId());
            createNewOrder.executeUpdate();
            try (ResultSet keys = createNewOrder.getGeneratedKeys()) {
                if (keys.next() == true) {
                    orderId = keys.getLong(1);
                }
            }
            insertToOrderItems = connection.prepareStatement(INSERT_INTO_ORDER_ITEMS);

            for (Map.Entry<Item, Integer> entry :
                    goods.entrySet()) {
                insertToOrderItems.setLong(1, orderId);
                insertToOrderItems.setLong(2, entry.getKey().getProductID());
                insertToOrderItems.setString(3, entry.getKey().getProductName());
                insertToOrderItems.setInt(4, entry.getValue());
                insertToOrderItems.executeUpdate();
                ProductManager productManager = ProductManager.getInstance();
                synchronized (productManager) {
                    productManager.updateProductAfterPurchase(connection, entry.getKey().getProductID(), entry.getValue());
                }
            }
            connection.commit();
            request.getSession().setAttribute("orderId", orderId);
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            throw new DBException("cannot confirm check", e);
        } finally {
            JdbcUtils.closeClosable(insertToOrderItems, createNewOrder, connection);
        }
    }

    public List<Order> prepareChecksForView(User usr, HttpServletRequest req, int recordsPerPage) throws DBException {
        UserRole currentRole = usr.getRole();
        Connection connection = null;
        Statement st = null;
        PreparedStatement prepStat = null;
        ResultSet rsOrderQuantity = null;
        ResultSet rs = null;
        try {
            connection = dbManager.getConnection();
            st = connection.createStatement();
            String date = req.getParameter("checksForDate");
            if (date == null && req.getSession().getAttribute("orders") != null) {
                List orders = (List) req.getSession().getAttribute("orders");
                int ordersQuantity = orders.size();
                int noOfPages = (int) Math.ceil(ordersQuantity * 1.0 / recordsPerPage);
                req.getSession().setAttribute("noOfPages", noOfPages);
                return orders;
            }
            if (date == null) {
                rsOrderQuantity = st.executeQuery(" select curdate()");
                rsOrderQuantity.next();
                date = rsOrderQuantity.getString(1);
            }
            req.getSession().setAttribute("date", date);
            rsOrderQuantity = getOrders(req, prepStat, connection, usr, date);
            rsOrderQuantity.next();
            int ordersQuantity = rsOrderQuantity.getInt("quantity");
            int noOfPages = (int) Math.ceil(ordersQuantity * 1.0 / recordsPerPage);
            req.getSession().setAttribute("noOfPages", noOfPages);
            if (currentRole == UserRole.CASHIER) {
                req.getSession().setAttribute("rolePage", "cashier_page");
                prepStat = connection.prepareStatement(SELECT_CHECKS_FOR_CASHIER);
                prepStat.setLong(1, usr.getId());
            } else {
                req.getSession().setAttribute("rolePage", "admin_page");
                if (date == null) {
                    prepStat = connection.prepareStatement(SELECT_CHECKS_FOR_SENIOR_CASHIER);
                } else {
                    prepStat = connection.prepareStatement(SELECT_CHECKS_FOR_SENIOR_CASHIER_WITH_DATE);
                    //Date date = new Date();
                    //Timestamp timeStamp = new Timestamp(date.getTime());
                    //prepStat.setObject(1, Instant.now().atZone(ZoneId.of("Europe/Kiev")).toLocalDate());
                    prepStat.setString(1, date);
                    // prepStat.setInt(2, recordsPerPage);
                    //prepStat.setInt(3, (page - 1) * recordsPerPage);
                }
                // req.setAttribute("currentPage", page);
            }
            rs = prepStat.executeQuery();
            List<Order> orders = extractOrders(rs);
            return orders;
        } catch (SQLException e) {
            logger.error("cannot do showChecks", e);
            throw new DBException("cannot do showChecks", e);
        } finally {
            JdbcUtils.closeClosable(rs, rsOrderQuantity, st, prepStat, connection);
        }
    }

    /**
     * Return orders for user request
     *
     * @param request
     * @param prepStat
     * @param connection
     * @param usr
     * @return orders quantity obtained by sql request
     * @throws SQLException if something went wrong during prepared statement execution
     */
    private ResultSet getOrders(HttpServletRequest request, PreparedStatement prepStat, Connection connection, User usr, String date) throws SQLException {
        switch (usr.getRole()) {
            case CASHIER: {
                request.getSession().setAttribute("rolePage", "cashier_page");
                prepStat = connection.prepareStatement(COUNT_ALL_ORDERS_FROM_DATE_FOR_CASHIER);
                prepStat.setLong(1, usr.getId());
                break;
            }
            case SENIOR_CASHIER: {
                String sqlRequest = (date == null) ? COUNT_ALL_ORDERS : COUNT_ALL_ORDERS_FROM_DATE + "'" + date + "'";
                prepStat = connection.prepareStatement(sqlRequest);
                break;
            }
        }
        ResultSet rs = prepStat.executeQuery();
        return rs;
    }

    /**
     * extract orders from resulSet and return List<Order>
     *
     * @param rs
     * @return List<Order>
     * @throws SQLException
     */
    private List<Order> extractOrders(ResultSet rs) throws SQLException {
        Map<Long, Order> orders = new HashMap<>();
        Item item;
        while (rs.next()) {
            long orderItemOrderID = rs.getLong("o.id");
            if (orders.containsKey(orderItemOrderID)) {
                item = ProductManager.getInstance().extractItemFromOrdersItems(rs);
            } else {
                Order orderItem = new Order();
                orderItem.setId(orderItemOrderID);
                orderItem.setCashierId(rs.getLong("created_by"));
                orderItem.setDatetime(rs.getTimestamp("date"));
                item = ProductManager.getInstance().extractItemFromOrdersItems(rs);
                orders.put(orderItemOrderID, orderItem);
            }
            orders.get(orderItemOrderID).getOrderItems().add(item);
        }
        List<Order> list = new ArrayList<>(orders.values());
        list.sort((a, b) -> (int) (b.getId() - a.getId()));
        return list;
    }

    /**
     * return today orders. Default method for check controller without date parameter in the request
     *
     * @param request
     * @return List<Order>
     * @throws DBException
     */
    public List<Order> getTodayChecks(HttpServletRequest request) throws DBException {
        Connection connection = null;

        Statement st = null;
        ResultSet rs = null;
        try {
            connection = dbManager.getConnection();
            st = connection.createStatement();
            rs = st.executeQuery(SELECT_CHECKS_FOR_SENIOR_CASHIER);
            return extractOrders(rs);
        } catch (SQLException e) {
            throw new DBException("cannot get orders for z-report", e);
        } finally {
            JdbcUtils.closeClosable(rs, st, connection);
        }
    }
}

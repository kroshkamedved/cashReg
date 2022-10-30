package com.elearn.logic;

import com.elearn.exception.DBException;
import com.elearn.db.entity.ItemDTO;
import com.elearn.db.entity.Order;
import com.elearn.db.entity.User;
import com.elearn.db.entity.UserRole;
import com.elearn.db.utils.DBManager;
import com.elearn.db.utils.JdbcUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.sql.*;
import java.util.*;
import java.util.Date;

public class CheckManager {
    private Logger logger = LogManager.getLogger(CheckManager.class);
    private static final String INSERT_INTO_ORDER_ITEMS = "insert into order_items (order_id,product_id, product_name,quantity) values (?,?,?,?)";
    private static final String SELECT_CHECKS_FOR_CASHIER = "select * from order_items oi " +
            "                                                       join orders o on o.id = oi.order_id" +
            "                                                        join goods g on oi.product_id = g.id " +
            "                                                           and o.created_by = ? " +
            "                                                           and cast(o.date as  date) = current_date()" +
            "                                                       order by o.id desc";
    private static final String SELECT_CHECKS_FOR_SENIOR_CASHIER = "select o.id, o.date, o.created_by, oi.product_id, oi.product_name, g.description, oi.quantity, g.price, g.units_id from order_items oi join orders o on o.id = oi.order_id join goods g on oi.product_id = g.id and o.created_by = 11 and cast(o.date as  date) = current_date() order by o.id desc";
    private final String INSERT_INTO_ORDERS = "Insert into orders (date,created_by) VALUES (?,?)";
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

    //TODO reduce stock quantity after new order
    public void confirmCheck(HttpServletRequest request, HashMap<ItemDTO, Integer> goods) throws DBException {
        Connection connection = null;
        PreparedStatement createNewOrder = null;
        PreparedStatement insertToOrderItems = null;
        long orderId = 0;
        try {
            connection = dbManager.getConnection();
            // connection.setAutoCommit(false);
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

            for (Map.Entry<ItemDTO, Integer> entry :
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

    public void showChecks(User usr, HttpServletRequest req) throws DBException {
        UserRole currentRole = usr.getRole();
        Connection connection = null;
        PreparedStatement prepStat = null;
        ResultSet rs = null;
        try {
            connection = dbManager.getConnection();
            if (currentRole == UserRole.CASHIER) {
                prepStat = connection.prepareStatement(SELECT_CHECKS_FOR_CASHIER);
                prepStat.setLong(1, usr.getId());
            } else {
                prepStat = connection.prepareStatement(SELECT_CHECKS_FOR_SENIOR_CASHIER);
                Date date = new Date();
                Timestamp timeStamp = new Timestamp(date.getTime());
                prepStat.setTimestamp(1, timeStamp);
            }
            rs = prepStat.executeQuery();
            List<Order> orders = extractOrders(rs);
            req.getSession().setAttribute("orders", orders);
        } catch (SQLException e) {
            logger.error("cannot do showChecks", e);
            throw new DBException("cannot do showChecks", e);
        } finally {
            JdbcUtils.closeClosable(rs, prepStat, connection);
        }
    }

    private List<Order> extractOrders(ResultSet rs) throws SQLException {
        Map<Long, Order> orders = new HashMap<>();
        ItemDTO item;
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
        return new ArrayList<>(orders.values());
    }
}

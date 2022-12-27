package com.elearn.fp.db.dao;

import com.elearn.fp.db.entity.Item;
import com.elearn.fp.db.entity.Order;
import com.elearn.fp.db.entity.User;
import com.elearn.fp.db.entity.UserRole;
import com.elearn.fp.db.utils.DBManager;
import com.elearn.fp.db.utils.JdbcUtils;
import com.elearn.fp.exception.DBException;
import com.elearn.fp.service.ProductManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.sql.*;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.elearn.fp.db.query.Query.*;
import static com.elearn.fp.service.CheckManager.extractOrders;

public class CheckDAO {
    Logger logger = LogManager.getLogger(CheckDAO.class);
    public static DBManager dbManager;

    public CheckDAO() {
        if (dbManager == null) {
            try {
                dbManager = DBManager.getInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public long confirmCheck(User usr, HashMap<Item, Integer> goods) throws DBException {
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
                    productManager.updateProductAfterPurchase(entry.getKey().getProductID(), entry.getValue());
                }
            }
            connection.commit();
            logger.trace("\"confirm check\" transaction successfully finished");
            return orderId;
        } catch (SQLException e) {
            try {
                connection.rollback();
                logger.error("\"confirm check\"  transaction rollback");
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            throw new DBException("cannot confirm check", e);
        } finally {
            JdbcUtils.closeClosable(insertToOrderItems, createNewOrder, connection);
        }
    }

    /**
     * Return orders for user request
     *
     * @param usr
     * @param recordsPerPage
     * @param date
     * @return orders quantity obtained by sql request
     * @throws SQLException if something went wrong during prepared statement execution
     */
    public List<Order> prepareChecksForView(User usr, int recordsPerPage, String date) throws DBException {
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
                if (date == null) {
                    prepStat = connection.prepareStatement(SELECT_CHECKS_FOR_SENIOR_CASHIER);
                } else {
                    prepStat = connection.prepareStatement(SELECT_CHECKS_FOR_SENIOR_CASHIER_WITH_DATE);
                    prepStat.setString(1, date);
                }
            }
            rs = prepStat.executeQuery();
            List<Order> orders = extractOrders(rs);
            return orders;
        } catch (SQLException e) {
            logger.error("cannot do showChecks", e);
            throw new DBException("cannot do showChecks", e);
        } finally {
            JdbcUtils.closeClosable(rs, prepStat, connection);
        }
    }

    public List<Order> getTodayChecks() throws DBException {
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


    public String getCurrentDate() throws DBException {
        Connection connection = null;
        Statement st = null;
        ResultSet rsOrderQuantity = null;
        try {
            connection = dbManager.getConnection();
            st = connection.createStatement();
            rsOrderQuantity = st.executeQuery(" select curdate()");
            rsOrderQuantity.next();
            return rsOrderQuantity.getString(1);
        } catch (SQLException e) {
            throw new DBException("can not getCurrentDate", e);
        } finally {
            JdbcUtils.closeClosable(rsOrderQuantity, st, connection);
        }

    }

    public int getOrderQuantity(User usr, String date) throws DBException {
        Connection connection = null;
        PreparedStatement prepStat = null;
        ResultSet rsOrderQuantity = null;
        try {
            connection = dbManager.getConnection();

            switch (usr.getRole()) {
                case CASHIER: {
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
            rsOrderQuantity = prepStat.executeQuery();
            rsOrderQuantity.next();
            int ordersQuantity = rsOrderQuantity.getInt("quantity");
            return ordersQuantity;
        } catch (SQLException e) {
            logger.error("can not get order quantity");
            throw new DBException("can not get order quantity", e);
        } finally {
            JdbcUtils.closeClosable(rsOrderQuantity, prepStat, connection);
        }
    }
}

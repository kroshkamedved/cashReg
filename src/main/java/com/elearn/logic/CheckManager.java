package com.elearn.logic;

import com.elearn.db.DBException;
import com.elearn.db.entity.ItemDTO;
import com.elearn.db.entity.User;
import com.elearn.db.utils.DBManager;
import com.elearn.db.utils.JdbcUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.sql.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CheckManager {
    public static final String INSERT_INTO_ORDER_ITEMS = "insert into order_items (order_id,product_id, product_name,quantity) values (?,?,?,?)";
    private final String INSERT_INTO_ORDERS = "Insert into orders (date,created_by) VALUES (?,?)";
    private static CheckManager instance;

    private Logger logger = LogManager.getLogger(CheckManager.class);

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

}

package com.elearn.logic;

import com.elearn.db.DBException;
import com.elearn.db.entity.ItemDTO;
import com.elearn.db.utils.DBManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.sql.*;

public class ProductManager {

    private static ProductManager instance;

    String INSERT_STATEMENT_SQL = "insert into goods(name,description,price, units_id) VALUES (?, ?, ?,?)";
    String INSERT_TO_WAREHOUSE_SQL = "insert into warehouse(product_id,quantity) VALUES (?, ?)";
    String UPDATE_ITEM_STOCK_SQL = "UPDATE warehouse SET QUANTITY = ? where product_id = ? ";
    private Logger logger = LogManager.getLogger(ProductManager.class);

    public static synchronized ProductManager getInstance() {
        if (instance == null) {
            instance = new ProductManager();
        }
        return instance;
    }

    DBManager dbManager;

    private ProductManager() {
        dbManager = DBManager.getInstance();
    }

    private static ItemDTO extractProduct(HttpServletRequest req) {
        String productName = req.getParameter("prod_name");
        String productDescription = req.getParameter("description");
        int productQuantity = Integer.parseInt(req.getParameter("prod_quantity"));
        int productUnitId = Integer.parseInt(req.getParameter("unit_id"));
        int productPrice = Integer.parseInt(req.getParameter("product_price"));
        return new ItemDTO(productName, productDescription, productQuantity, productUnitId, productPrice);
    }

    public void createProduct(HttpServletRequest request) throws DBException {
        ItemDTO product = extractProduct(request);
        Connection connection = null;
        PreparedStatement insertProductStmnt = null;
        PreparedStatement insertToWarehouseStmnt = null;
        ResultSet resultSet = null;
        try {
            connection = dbManager.getConnection();
            connection.setAutoCommit(false);

            insertProductStmnt = dbManager.getConnection().prepareStatement(INSERT_STATEMENT_SQL, Statement.RETURN_GENERATED_KEYS);
            insertProductStmnt.setString(1, product.getProductName());
            insertProductStmnt.setString(2, product.getProductDescription());
            insertProductStmnt.setInt(3, product.getProductPrice());
            insertProductStmnt.setInt(4, product.getProductUnitId());
            insertProductStmnt.executeUpdate();


            resultSet = insertProductStmnt.getGeneratedKeys();

            if (resultSet.next()) {
                product.setProductID(resultSet.getLong(1));
            }

            insertToWarehouseStmnt = connection.prepareStatement(INSERT_TO_WAREHOUSE_SQL);
            insertToWarehouseStmnt.setLong(1, product.getProductID());
            insertToWarehouseStmnt.setInt(2, product.getProductQuantity());
            insertToWarehouseStmnt.executeUpdate();

            connection.commit();
            request.getSession().setAttribute("product", product);

        } catch (SQLException x) {
            try {
                if (connection != null)
                    connection.rollback();
                logger.error("Database was thrown SQLException with message: {} {}", x.getErrorCode(), x.getMessage());
                throw new DBException("cannot add product", x);
            } catch (SQLException e) {
                logger.fatal("Database was thrown SQLException during closing connection: {} {}", e.getErrorCode(), e.getMessage());
            }
        } finally {
            try {
                insertProductStmnt.close();
            } catch (Exception e) {
            }
            try {
                insertToWarehouseStmnt.close();
            } catch (Exception e) {
            }
            try {
                resultSet.close();
            } catch (Exception e) {
            }
            try {
                connection.close();
            } catch (Exception e) {
            }
        }
    }

    public void updateProduct(HttpServletRequest req) throws DBException {
        Connection connection = null;
        PreparedStatement updateItemStock = null;
        ResultSet resultSet = null;
        try {
            connection = dbManager.getConnection();

            updateItemStock = dbManager.getConnection().prepareStatement(UPDATE_ITEM_STOCK_SQL);
            updateItemStock.setInt(1, Integer.parseInt(req.getParameter("newStock")));
            updateItemStock.setInt(2, Integer.parseInt(req.getParameter("productId")));
            updateItemStock.executeUpdate();


        } catch (SQLException e) {
            logger.error("cannot update product stock");
            throw new DBException("cannot update product stock", e);
        } finally {
            try {
                updateItemStock.close();
            } catch (Exception e) {
            }
            try {
                resultSet.close();
            } catch (Exception e) {
            }
            try {
                connection.close();
            } catch (Exception e) {
            }
        }
        logger.trace("product successfully updated");
    }
}

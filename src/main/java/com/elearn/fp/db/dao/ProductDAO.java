package com.elearn.fp.db.dao;

import com.elearn.fp.db.entity.Item;
import com.elearn.fp.db.entity.Unit;
import com.elearn.fp.db.utils.DBManager;
import com.elearn.fp.db.utils.JdbcUtils;
import com.elearn.fp.exception.DBException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.elearn.fp.db.query.Query.*;
import static com.elearn.fp.service.ProductManager.extractItemFromGoodsTable;

public class ProductDAO {

    Logger logger = LogManager.getLogger(ProductDAO.class);
    public static DBManager dbManager;

    public ProductDAO() {
        if (dbManager == null) {
            try {
                dbManager = DBManager.getInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void createProduct(Item product) throws DBException {
        Connection connection = null;
        PreparedStatement insertProductStmnt = null;
        PreparedStatement insertToWarehouseStmnt = null;
        ResultSet resultSet = null;
        try {
            connection = dbManager.getConnection();
            connection.setAutoCommit(false);

            insertProductStmnt = connection.prepareStatement(INSERT_STATEMENT_SQL, Statement.RETURN_GENERATED_KEYS);
            insertProductStmnt.setString(1, product.getProductName());
            insertProductStmnt.setString(2, product.getProductDescription());
            insertProductStmnt.setDouble(3, product.getProductPrice());
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

        } catch (SQLException x) {
            try {
                if (connection != null) {
                    connection.rollback();
                }
                logger.error("Database was thrown SQLException with message: {} {}", x.getErrorCode(), x.getMessage());
                throw new DBException("cannot add product", x);
            } catch (SQLException e) {
                logger.fatal("Database was thrown SQLException during closing connection: {} {}", e.getErrorCode(), e.getMessage());
            }
        } finally {
            JdbcUtils.closeClosable(resultSet, insertProductStmnt, insertToWarehouseStmnt, connection);
        }
    }

    public List<Unit> getUnitList() throws DBException {
        List<Unit> listCategory = new ArrayList<>();
        Statement statement = null;
        ResultSet result = null;
        Connection connection = null;

        try {
            connection = DBManager.getInstance().getConnection();
            String sql = "SELECT * FROM units ORDER BY id";
            statement = connection.createStatement();
            result = statement.executeQuery(sql);

            while (result.next()) {
                int id = result.getInt("id");
                String name = result.getString("unit");
                Unit unit = new Unit(id, name);
                listCategory.add(unit);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DBException("cannot get measure units list", ex);
        } finally {
            JdbcUtils.closeClosable(result, statement, connection);
        }
        return listCategory;
    }

    public void updateProductAfterPurchase(int newStock, long productId) throws DBException {
        Connection connection = null;
        PreparedStatement updateItemStock = null;
        try {
            connection = dbManager.getConnection();
            updateItemStock = connection.prepareStatement(UPDATE_ITEM_STOCK_SQL);
            updateItemStock.setInt(1, newStock);
            updateItemStock.setLong(2, productId);
            updateItemStock.executeUpdate();
        } catch (SQLException e) {
            logger.error("cannot update product stock");
            throw new DBException("cannot update product stock", e);
        } finally {
            JdbcUtils.closeClosable(updateItemStock, connection);
        }
        logger.trace("product successfully updated");
    }

    public void deleteProduct(long id) throws DBException {
        Connection connection = null;
        PreparedStatement deleteItem = null;

        try {
            connection = dbManager.getConnection();
            deleteItem = connection.prepareStatement(DELETE_ITEM_FROM_STOCK);
            deleteItem.setLong(1, id);
            deleteItem.executeUpdate();
        } catch (SQLException e) {
            logger.error("cannot delete product from stock");
            throw new DBException("cannot delete product from stock", e);
        } finally {
            JdbcUtils.closeClosable(deleteItem, connection);
        }
        logger.trace("product successfully deleted");
    }

    public List<Item> getAllGoods() throws DBException {
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        List<Item> itemList = new ArrayList<>();
        try {
            connection = dbManager.getConnection();
            statement = connection.createStatement();
            rs = statement.executeQuery(SELECT_ALL_GOODS);
            while (rs.next()) {
                itemList.add(extractItemFromGoodsTable(rs));
            }
        } catch (SQLException e) {
            logger.error("cannot get goods from DB");
            throw new DBException("cannot get goods list", e);
        } finally {
            JdbcUtils.closeClosable(rs, statement, connection);
        }
        logger.trace("All goods returned");
        return itemList;
    }

    public List<Item> getGoods(int page, int recordsPerPage) throws DBException {
        Connection connection = null;
        Statement st = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        List<Item> itemList = new ArrayList<>();
        try {
            connection = dbManager.getConnection();
            ps = connection.prepareStatement(SELECT_GOODS_WITH_LIMIT);
            ps.setInt(1, recordsPerPage);
            ps.setInt(2, (page - 1) * recordsPerPage);
            rs = ps.executeQuery();
            while (rs.next()) {
                itemList.add(extractItemFromGoodsTable(rs));
            }
        } catch (SQLException e) {
            logger.error("cannot get goods from DB");
            throw new DBException("cannot get goods list", e);
        } finally {
            JdbcUtils.closeClosable(rs, ps, st, connection);
        }
        logger.trace("paginated goods shown");
        return itemList;
    }

    public int getNumberOfPage(int recordsPerPage) throws DBException {
        Connection connection = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            connection = dbManager.getConnection();
            st = connection.createStatement();
            ResultSet rs1 = st.executeQuery(COUNT_ALL_GOODS);
            rs1.next();
            int goodsQuantity = rs1.getInt("quantity");
            return (int) Math.ceil(goodsQuantity * 1.0 / recordsPerPage);
        } catch (SQLException e) {
            throw new DBException("can't count number of page", e);
        } finally {
            JdbcUtils.closeClosable(rs, st, connection);
        }
    }

    public void addProductToCart(String identifier, HashMap<Item, Integer> cart) throws DBException {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = dbManager.getConnection();
            ps = connection.prepareStatement(SELECT_ITEM_DTO);
            try {
                ps.setInt(1, Integer.parseInt(identifier));
            } catch (Exception e) {
                ps.setInt(1, -1);
                logger.trace("addProductToCart not by ID");
            }
            ps.setString(2, identifier);
            rs = ps.executeQuery();
            while (rs.next()) {
                cart.put(extractItemFromGoodsTable(rs), 1);
            }
            logger.trace("product putted in cart");
        } catch (SQLException e) {
            logger.error("cannot do addProductToCart");
            throw new DBException("cannot do addProductToCart", e);
        } finally {
            JdbcUtils.closeClosable(rs, ps, connection);
        }
        logger.trace("product successfully added to cart");
    }

    public void deleteItemFromOrder(long orderId, long deleteItemId, int productQuantity) throws DBException {
        Connection connection = null;
        PreparedStatement ps = null;
        PreparedStatement psIncreaseStock = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            connection = DBManager.getInstance().getConnection();
            connection.setAutoCommit(false);

            ps = connection.prepareStatement(DELETE_ITEM_FROM_ORDER);

            ps.setLong(1, orderId);
            ps.setLong(2, deleteItemId);
            ps.executeUpdate();


            psIncreaseStock = connection.prepareStatement(UDPATE_WAREHOUSE_AFTER_ITEM_DELETED_FROM_ORDER);
            psIncreaseStock.setInt(1, productQuantity);
            psIncreaseStock.setLong(2, deleteItemId);
            psIncreaseStock.executeUpdate();

            st = connection.createStatement();
            rs = st.executeQuery(COUNT_ORDER_ITEMS + orderId);
            rs.next();
            if (rs.getInt(1) == 0) {
                st.executeQuery(DELETE_ORDER_WITH_ID + orderId);
            }
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new DBException("error during connection rollback", ex);
            }
            logger.error("cannot delete Item From Order");
            throw new DBException("cannot delete Item From Order", e);
        } finally {
            JdbcUtils.closeClosable(psIncreaseStock, st, ps, connection);
        }
        logger.trace("product successfully deleted from order");
    }

    public void deleteWholeOrder(long deleteOrderId) throws DBException {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = DBManager.getInstance().getConnection();
            ps = connection.prepareStatement("DELETE FROM orders where id = ?");
            ps.setLong(1, deleteOrderId);
            ps.executeUpdate();
        } catch (SQLException e) {
            logger.error("cannot do deleteWholeOrder");
            throw new DBException("error during connection rollback", e);
        } finally {
            JdbcUtils.closeClosable(ps, connection);
        }
        logger.trace("order successfully deleted from order");
    }

}

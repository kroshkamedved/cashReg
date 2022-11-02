package com.elearn.fp.logic;

import com.elearn.fp.exception.DBException;
import com.elearn.fp.db.entity.ItemDTO;
import com.elearn.fp.db.entity.Unit;
import com.elearn.fp.db.utils.DBManager;
import com.elearn.fp.db.utils.JdbcUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProductManager {

    private static ProductManager instance;

    private final String INSERT_STATEMENT_SQL = "insert into goods(name,description,price, units_id) VALUES (?, ?, ?,?)";
    private final String INSERT_TO_WAREHOUSE_SQL = "insert into warehouse(product_id,quantity) VALUES (?, ?)";
    private final String UPDATE_ITEM_STOCK_SQL = "UPDATE warehouse SET QUANTITY = ? where product_id = ? ";
    private final String UPDATE_ITEM_STOCK_AFTER_PURCHASE = "UPDATE warehouse SET QUANTITY = (QUANTITY - ?) where product_id = ? ";
    private final String DELETE_ITEM_FROM_STOCK = "DELETE FROM goods WHERE ID = ? ";
    private final String SELECT_ALL_GOODS = "SELECT * FROM epam_project_db.goods gd join warehouse wh on gd.id = wh.product_id";
    private final String SELECT_ITEM_DTO = "SELECT * FROM epam_project_db.goods gd join warehouse wh on gd.id = wh.product_id where gd.id = ? or gd.name = ?";
    private final String COUNT_ALL_GOODS = "SELECT COUNT(*) as quantity FROM goods";
    private final String SELECT_GOODS_WITH_LIMIT = "SELECT * FROM goods gd join warehouse wh on gd.id = wh.product_id ORDER BY name ASC LIMIT ? offset ?";

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
        double productPrice = Double.parseDouble(req.getParameter("product_price"));
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
            request.getSession().setAttribute("product", product);

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

    public void updateProductAfterPurchase(HttpServletRequest req) throws DBException {
        Connection connection = null;
        PreparedStatement updateItemStock = null;
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
            JdbcUtils.closeClosable(updateItemStock, connection);
        }
        logger.trace("product successfully updated");
    }

    public void updateProductAfterPurchase(Connection connection, long id, int newStock) throws DBException {
        PreparedStatement updateItemStock = null;
        try {
            connection = dbManager.getConnection();

            updateItemStock = connection.prepareStatement(UPDATE_ITEM_STOCK_AFTER_PURCHASE);
            updateItemStock.setInt(1, newStock);
            updateItemStock.setLong(2, id);
            updateItemStock.executeUpdate();

        } catch (SQLException e) {
            logger.error("cannot update product stock");
            throw new DBException("cannot update product stock", e);
        } finally {
            JdbcUtils.closeClosable(updateItemStock, connection);
        }
        logger.trace("product successfully updated");
    }

    public void deleteProduct(HttpServletRequest req) throws DBException {
        Connection connection = null;
        PreparedStatement deleteItem = null;

        try {
            connection = dbManager.getConnection();

            deleteItem = dbManager.getConnection().prepareStatement(DELETE_ITEM_FROM_STOCK);
            deleteItem.setInt(1, Integer.parseInt(req.getParameter("deleteItemId")));
            deleteItem.executeUpdate();


        } catch (SQLException e) {
            logger.error("cannot delete product from stock");
            throw new DBException("cannot delete product from stock", e);
        } finally {
            JdbcUtils.closeClosable(deleteItem, connection);
        }
        logger.trace("product successfully deleted");
    }

    public List<ItemDTO> getAllGoods() throws DBException {
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        List<ItemDTO> itemDTOList = new ArrayList<>();
        try {
            connection = dbManager.getConnection();
            statement = connection.createStatement();
            rs = statement.executeQuery(SELECT_ALL_GOODS);

            while (rs.next()) {
                itemDTOList.add(extractItemFromGoodsTable(rs));
            }
        } catch (SQLException e) {
            logger.error("cannot get goods from DB");
            throw new DBException("cannot get goods list", e);
        } finally {
            JdbcUtils.closeClosable(rs, statement, connection);
        }
        logger.trace("All goods returned");
        return itemDTOList;
    }

    protected ItemDTO extractItemFromGoodsTable(ResultSet rs) throws SQLException {
        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setProductID(rs.getInt("id"));
        itemDTO.setProductName(rs.getString("name"));
        itemDTO.setProductDescription(rs.getString("description"));
        itemDTO.setProductPrice(rs.getInt("price"));
        itemDTO.setProductUnitId(rs.getInt("units_id"));
        itemDTO.setProductQuantity(rs.getInt("quantity"));

        return itemDTO;
    }

    protected ItemDTO extractItemFromOrdersItems(ResultSet rs) throws SQLException {
        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setProductID(rs.getInt("product_id"));
        itemDTO.setProductName(rs.getString("product_name"));
        itemDTO.setProductDescription(rs.getString("description"));
        itemDTO.setProductPrice(rs.getInt("price"));
        itemDTO.setProductUnitId(rs.getInt("units_id"));
        itemDTO.setProductQuantity(rs.getInt("quantity"));

        return itemDTO;
    }

    public List<ItemDTO> getGoods(HttpServletRequest req, int page, int recordsPerPage) throws DBException {
        Connection connection = null;
        Statement st = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        List<ItemDTO> itemDTOList = new ArrayList<>();
        try {
            connection = dbManager.getConnection();
            st = connection.createStatement();
            ResultSet rs1 = st.executeQuery(COUNT_ALL_GOODS);
            rs1.next();
            int goodsQuantity = rs1.getInt("quantity");

            int noOfPages = (int) Math.ceil(goodsQuantity * 1.0 / recordsPerPage);

            ps = connection.prepareStatement(SELECT_GOODS_WITH_LIMIT);
            ps.setInt(1, recordsPerPage);
            ps.setInt(2, (page - 1) * recordsPerPage);

            rs = ps.executeQuery();

            req.setAttribute("noOfPages", noOfPages);
            req.setAttribute("currentPage", page);

            while (rs.next()) {
                itemDTOList.add(extractItemFromGoodsTable(rs));
            }
        } catch (SQLException e) {
            logger.error("cannot get goods from DB");
            throw new DBException("cannot get goods list", e);
        } finally {
            JdbcUtils.closeClosable(rs, ps, st, connection);
        }
        logger.trace("paginated goods shown");
        return itemDTOList;
    }

    public void addProductToCart(HttpServletRequest req) throws DBException {
        String identifier = req.getParameter("prod_identifier");
        //List<ItemDTO> cart = (List<ItemDTO>) req.getSession().getAttribute("cart");
        HashMap<ItemDTO, Integer> cart = (HashMap<ItemDTO, Integer>) req.getSession().getAttribute("cart");
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
                //cart.add(extractItem(rs));
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

    public void deleteItemFromOrder(HttpServletRequest req) throws DBException {
        Connection connection = null;
        PreparedStatement ps = null;
        PreparedStatement psIncreaseStock = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            connection = DBManager.getInstance().getConnection();
            connection.setAutoCommit(false);

            ps = connection.prepareStatement("delete from order_items where order_id = ? and product_id = ?");
            int orderId = Integer.parseInt(req.getParameter("orderId"));
            ps.setInt(1, orderId);
            ps.setInt(2, Integer.parseInt(req.getParameter("deleteItemId")));
            ps.executeUpdate();


            psIncreaseStock = connection.prepareStatement("UPDATE warehouse SET quantity = (quantity + ?) WHERE product_id  = ?");
            psIncreaseStock.setInt(1, Integer.parseInt(req.getParameter("productQuantity")));
            psIncreaseStock.setInt(2, Integer.parseInt(req.getParameter("deleteItemId")));
            psIncreaseStock.executeUpdate();

            st = connection.createStatement();
            rs = st.executeQuery("select count(*) as oiCount from order_items where order_id ="+ orderId);
            rs.next();
            if(rs.getInt(1) == 0)
            {
                st.executeQuery("DELETE FROM orders where id =" + orderId);
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
            JdbcUtils.closeClosable(psIncreaseStock, ps, connection);
        }
        logger.trace("product successfully deleted from order");
    }

    public void deleteWholeOrder(HttpServletRequest req) throws DBException {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = DBManager.getInstance().getConnection();
            ps = connection.prepareStatement("DELETE FROM orders where id = ?");
            ps.setLong(1, Integer.parseInt(req.getParameter("deleteOrderId")));
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

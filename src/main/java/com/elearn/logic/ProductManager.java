package com.elearn.logic;

import com.elearn.db.DBException;
import com.elearn.db.entity.ItemDTO;
import com.elearn.db.utils.DBManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductManager {

    private static ProductManager instance;

    String INSERT_STATEMENT_SQL = "insert into goods(name,description,price, units_id) VALUES (?, ?, ?,?)";
    String INSERT_TO_WAREHOUSE_SQL = "insert into warehouse(product_id,quantity) VALUES (?, ?)";
    String UPDATE_ITEM_STOCK_SQL = "UPDATE warehouse SET QUANTITY = ? where product_id = ? ";
    String DELETE_ITEM_FROM_STOCK = "DELETE FROM goods WHERE ID = ? ";

    String SELECT_ALL_GOODS = "SELECT * FROM epam_project_db.goods gd join warehouse wh on gd.id = wh.product_id";
    String COUNT_ALL_GOODS = "SELECT COUNT(*) as quantity FROM goods";
    String SELECT_GOODS_WITH_LIMIT = "SELECT * FROM goods gd join warehouse wh on gd.id = wh.product_id ORDER BY name ASC LIMIT ? offset ?";

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

    public void deleteProduct(HttpServletRequest req) throws DBException {
        Connection connection = null;
        PreparedStatement deleteItem = null;
        ResultSet resultSet = null;

        try {
            connection = dbManager.getConnection();

            deleteItem = dbManager.getConnection().prepareStatement(DELETE_ITEM_FROM_STOCK);
            deleteItem.setInt(1, Integer.parseInt(req.getParameter("deleteItemId")));
            deleteItem.executeUpdate();


        } catch (SQLException e) {
            logger.error("cannot delete product from stock");
            throw new DBException("cannot delete product from stock", e);
        } finally {
            try {
                deleteItem.close();
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
                itemDTOList.add(extractItem(rs));
            }
        } catch (SQLException e) {
            logger.error("cannot get goods from DB");
            throw new DBException("cannot get goods list", e);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return itemDTOList;
    }

    private ItemDTO extractItem(ResultSet rs) throws SQLException {
        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setProductID(rs.getInt("id"));
        itemDTO.setProductName(rs.getString("name"));
        itemDTO.setProductDescription(rs.getString("description"));
        itemDTO.setProductPrice(rs.getInt("price"));
        itemDTO.setProductUnitId(rs.getInt("units_id"));
        itemDTO.setProductQuantity(rs.getInt("quantity"));

        return itemDTO;
    }

    public List<ItemDTO> getGoods(HttpServletRequest req, int page, int recordsPerPage) throws DBException {
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        List<ItemDTO> itemDTOList = new ArrayList<>();
        try {
            connection = dbManager.getConnection();
            Statement st = connection.createStatement();
            ResultSet rs1 = st.executeQuery(COUNT_ALL_GOODS);
            rs1.next();
            int goodsQuantity = rs1.getInt("quantity");

            int noOfPages = (int) Math.ceil(goodsQuantity * 1.0
                    / recordsPerPage);

            ps = connection.prepareStatement(SELECT_GOODS_WITH_LIMIT);
            ps.setInt(1, recordsPerPage);
            ps.setInt(2, (page - 1) * recordsPerPage);

            rs = ps.executeQuery();

            req.setAttribute("noOfPages", noOfPages);
            req.setAttribute("currentPage", page);

            while (rs.next()) {
                itemDTOList.add(extractItem(rs));
            }
        } catch (SQLException e) {
            logger.error("cannot get goods from DB");
            throw new DBException("cannot get goods list", e);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return itemDTOList;
    }
}

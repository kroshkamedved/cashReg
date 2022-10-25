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

public class GoodsManager {

    private static GoodsManager instance;

    String SELECT_ALL_GOODS = "SELECT * FROM epam_project_db.goods gd join warehouse wh on gd.id = wh.product_id";
    String COUNT_ALL_GOODS = "SELECT COUNT(*) as quantity FROM goods";
    String SELECT_GOODS_WITH_LIMIT = "SELECT * FROM goods gd join warehouse wh on gd.id = wh.product_id ORDER BY name ASC LIMIT ? offset ?";
    private Logger logger = LogManager.getLogger(GoodsManager.class);

    public static synchronized GoodsManager getInstance() {
        if (instance == null) {
            instance = new GoodsManager();
        }
        return instance;
    }

    DBManager dbManager;

    private GoodsManager() {
        dbManager = DBManager.getInstance();
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

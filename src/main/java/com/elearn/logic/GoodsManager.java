package com.elearn.logic;

import com.elearn.db.DBException;
import com.elearn.db.entity.ItemDTO;
import com.elearn.db.utils.DBManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class GoodsManager {

    private static GoodsManager instance;

    String SELECT_ALL_GOODS = "SELECT * FROM epam_project_db.goods gd join warehouse wh on gd.id = wh.product_id";

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
                itemDTOList.add(extraxtItem(rs));
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

    private ItemDTO extraxtItem(ResultSet rs) throws SQLException {
        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setProductID(rs.getInt("id"));
        itemDTO.setProductName(rs.getString("name"));
        itemDTO.setProductDescription(rs.getString("description"));
        itemDTO.setProductPrice(rs.getInt("price"));
        itemDTO.setProductUnitId(rs.getInt("units_id"));
        itemDTO.setProductQuantity(rs.getInt("quantity"));

        return itemDTO;
    }
}

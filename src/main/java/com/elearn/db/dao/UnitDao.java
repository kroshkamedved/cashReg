package com.elearn.db.dao;

import com.elearn.db.utils.DBManager;
import com.elearn.db.entity.Unit;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UnitDao {

    public List<Unit> list() throws SQLException {
        List<Unit> listCategory = new ArrayList<>();

        try (Connection connection = DBManager.getInstance().getConnection()) {
            String sql = "SELECT * FROM units ORDER BY id";
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);

            while (result.next()) {
                int id = result.getInt("id");
                String name = result.getString("unit");
                Unit unit = new Unit(id, name);

                listCategory.add(unit);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw ex;
        }

        return listCategory;
    }
}

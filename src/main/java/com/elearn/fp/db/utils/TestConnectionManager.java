package com.elearn.fp.db.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestConnectionManager {

    private Connection connection;

    public Connection getConnection() throws SQLException {
        String connectionString = "jdbc:mariadb://192.168.88.21:3306/epam_project_db?characterEncoding=UTF-8";
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            connection = DriverManager.getConnection(connectionString, "db_user", "password");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
}


package com.elearn.db.utils;

import java.sql.*;
import java.util.ResourceBundle;

public class JdbcUtils {

    private static ResourceBundle resources = ResourceBundle.getBundle("database");
    private static boolean initialized;

    public static Connection getConnection() throws SQLException {
        if (!initialized) try {
            initializeDriver();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        String url = resources.getString("db.url");
        String login = resources.getString("db.user");
        String pass = resources.getString("db.password");
        Connection connection = DriverManager.getConnection(url, login, pass);
        return connection;
    }

    private static synchronized void initializeDriver() throws ClassNotFoundException {
        Class.forName(resources.getString("db.driver"));
        initialized = true;
    }

    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void closeStatement(Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void closePreparedStatement(PreparedStatement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void rollback(Connection connection) {
        if (connection != null) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


}

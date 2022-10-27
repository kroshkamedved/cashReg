package com.elearn.db.utils;

import java.sql.*;
import java.util.Arrays;
import java.util.ResourceBundle;

public class JdbcUtils {

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

    public static void closeClosable(AutoCloseable... autoCloseables) {
        for (AutoCloseable item :
                autoCloseables) {
            if (item != null) {
                try {
                    item.close();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}

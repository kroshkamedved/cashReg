package com.elearn.fp.db.utils;

import java.sql.*;
import java.util.Arrays;
import java.util.ResourceBundle;

public class JdbcUtils {
    /**
     * method for connection closing
     * @param connection
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * method for statement closing
     * @param statement
     */
    public static void closeStatement(Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * method for preparedstatement closing
     * @param statement
     */
    public static void closePreparedStatement(PreparedStatement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * method for connection rollback
     * @param connection
     */
    public static void rollback(Connection connection) {
        if (connection != null) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Allows to close any quantity of any object which implements autocloseable interface
     * @param autoCloseables
     */
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

package com.elearn.db.utils;

import com.elearn.db.entity.User;
import com.elearn.db.entity.UserRole;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBManager {

    private static DBManager instance;
    private static Logger logger = LogManager.getLogger(DBManager.class);

    public static DBManager getInstance() {
        if (instance == null) {
            instance = new DBManager();
        }
        return instance;
    }

    private final DataSource ds;

    private DBManager() {
        try {
            Context initContext = new InitialContext();
            Context envContext = (Context) initContext.lookup("java:/comp/env");
            ds = (DataSource) envContext.lookup("jdbc/mariadb");
            logger.info("dataSource ==> " + ds);
        } catch (NamingException ex) {
            throw new IllegalStateException("Cannot obtain a data source");
        }
    }

    public Connection getConnection() throws SQLException {
        return ds.getConnection();
    }






}

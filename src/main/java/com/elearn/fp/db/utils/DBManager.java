package com.elearn.fp.db.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Class which obtain datasource. Provides the connections for managers which work with db
 */
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

    /**
     * provides connection
     * @return Connection
     * @throws SQLException
     */
    public Connection getConnection() throws SQLException {
        return ds.getConnection();
    }






}

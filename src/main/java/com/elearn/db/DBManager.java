package com.elearn.db;

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

    private static final String FIND_USER_BY_NAME = "select * from users where name=? AND pass_hash=?";


    public Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    public User findUser(Connection con, String login, String password) throws SQLException {
        PreparedStatement prpst = null;
        ResultSet rs = null;
        try {
            prpst = con.prepareStatement(FIND_USER_BY_NAME);
            prpst.setString(1, login);
            prpst.setString(2, hashPass(password));

            rs = prpst.executeQuery();
            if (rs.next()) {
                return extractUser(rs);
            }

        } finally {
            if (rs != null) {
                rs.close();
            }
            if (prpst != null) {
                prpst.close();
            }
        }
        return null;
    }

    private static String hashPass(String password) {
        return "passhash" + password;
    }

    private static User extractUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setLogin(rs.getString("name"));
        user.setPassword(rs.getString("pass_hash"));
        user.setRole(UserRole.valueOf(rs.getString("role")));
        System.out.println(user);
        return user;
    }
}

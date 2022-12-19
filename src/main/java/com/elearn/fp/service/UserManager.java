package com.elearn.fp.service;

import com.elearn.fp.db.utils.JdbcUtils;
import com.elearn.fp.exception.DBException;
import com.elearn.fp.db.entity.UserRole;
import com.elearn.fp.db.utils.DBManager;
import com.elearn.fp.db.entity.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserManager {
    private static final String FIND_USER_BY_NAME = "select * from users where name=? AND pass_hash=?";
    private static UserManager instance;

    public static synchronized UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    DBManager dbManager;

    private UserManager() {
        dbManager = DBManager.getInstance();
    }

    public User findUser(String login, String password) throws DBException {
        try (Connection con = dbManager.getConnection()) {
            return findUser(con, login, password);
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DBException("Cannot find user", ex);
        }
    }

     static User extractUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setLogin(rs.getString("name"));
        user.setPassword(rs.getString("pass_hash"));
        user.setRole(UserRole.valueOf(rs.getString("role")));
        return user;
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
            JdbcUtils.closeClosable(prpst, rs);
        }
        return null;
    }

    private static String hashPass(String password) {
        return "passhash" + password;
    }
}

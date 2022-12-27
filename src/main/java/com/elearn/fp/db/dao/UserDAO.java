package com.elearn.fp.db.dao;

import com.elearn.fp.db.entity.User;
import com.elearn.fp.db.entity.UserRole;
import com.elearn.fp.db.utils.DBManager;
import com.elearn.fp.db.utils.JdbcUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.elearn.fp.db.query.Query.FIND_USER_BY_NAME;
import static com.elearn.fp.service.UserManager.extractUser;
import static com.elearn.fp.service.UserManager.hashPass;

public class UserDAO {
    public static DBManager dbManager;

    public UserDAO() {
        if (dbManager == null) {
            try {
                dbManager = DBManager.getInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * get the appropriate User, whose credential corresponds to method parameters;
     *
     * @param login
     * @param hashedPassword
     * @return User if login and password are correct, or null if user is absent or credentials are wrong.
     * @throws SQLException
     */
    public User getUser(String login, String hashedPassword) throws SQLException {
        Connection connection = null;
        PreparedStatement prpst = null;
        ResultSet rs = null;
        try {
            connection = dbManager.getConnection();
            prpst = connection.prepareStatement(FIND_USER_BY_NAME);
            prpst.setString(1, login);
            prpst.setString(2, hashedPassword);
            rs = prpst.executeQuery();
            if (rs.next()) {
                return extractUser(rs);
            }
        } finally {
            JdbcUtils.closeClosable(connection, rs, prpst);
        }
        return null;
    }
}

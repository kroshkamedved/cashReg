package com.elearn.fp.service;

import com.elearn.fp.db.dao.UserDAO;
import com.elearn.fp.db.utils.JdbcUtils;
import com.elearn.fp.exception.DBException;
import com.elearn.fp.db.entity.UserRole;
import com.elearn.fp.db.utils.DBManager;
import com.elearn.fp.db.entity.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.elearn.fp.db.query.Query.*;

/**
 * Service class, used for convenient way of dealing with model
 */
public class UserManager {

    Logger logger = LogManager.getLogger(UserManager.class);
    private static UserManager instance;

    public static synchronized UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    private UserDAO userDAO;

    private UserManager() {
        userDAO = new UserDAO();
    }

    /**
     * prepare credentials for dao request and return User
     *
     * @param login
     * @param password
     * @return
     * @throws DBException
     */
    public User findUser(String login, String password) throws DBException {
        try {
            String hashedPass = hashPass(password);
            return userDAO.getUser(login, hashedPass);
        } catch (SQLException ex) {
            logger.trace("Cannot find user");
            throw new DBException("Cannot find user", ex);
        }
    }

    /**
     * extract User from the ResulSet obtained from SQL query
     *
     * @param rs
     * @return
     * @throws SQLException
     */
    public static User extractUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setLogin(rs.getString("name"));
        user.setPassword(rs.getString("pass_hash"));
        user.setRole(UserRole.valueOf(rs.getString("role")));
        return user;
    }

    /**
     * imitation of real hashing algorithm.
     * Exist for a purpose of safe data storage
     *
     * @param password
     * @return
     */
    public static String hashPass(String password) {
        return "passhash" + password;
    }

}

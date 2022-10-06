package com.elearn.logic;

import com.elearn.db.DBException;
import com.elearn.db.DBManager;
import com.elearn.db.entity.User;

import java.sql.Connection;
import java.sql.SQLException;

public class UserManager {
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
            return dbManager.findUser(con, login, password);
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DBException("Cannot find user", ex);
        }
    }
}

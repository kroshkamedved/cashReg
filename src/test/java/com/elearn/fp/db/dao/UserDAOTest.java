package com.elearn.fp.db.dao;

import com.elearn.fp.db.entity.User;
import com.elearn.fp.db.entity.UserRole;
import com.elearn.fp.db.utils.DBManager;
import com.elearn.fp.db.utils.TestConnectionManager;
import com.elearn.fp.exception.DBException;
import com.elearn.fp.service.UserManager;
import org.junit.BeforeClass;
import org.junit.jupiter.api.*;

import javax.naming.NamingException;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserDAOTest {

    static private Connection connection;
    private User mykola;
    private UserDAO userDAO;
    private TestConnectionManager testConnectionManager;

    @BeforeEach
    void init() throws SQLException {
        mykola = new User();
        mykola.setRole(UserRole.SENIOR_CASHIER);
        mykola.setId(7);
        mykola.setLogin("Mykola");
        mykola.setPassword(UserManager.hashPass("1991"));

        testConnectionManager = new TestConnectionManager();
        connection = testConnectionManager.getConnection();
        DBManager dbManager = mock(DBManager.class);
        when(dbManager.getConnection()).thenReturn(connection);

        UserDAO.dbManager = dbManager;
        userDAO = new UserDAO();
    }

    @DisplayName("check getUser - assert true")
    @Test
    void getUserTestTrueAssumption() throws SQLException {
        User obtainedUser = userDAO.getUser("Mykola", "passhash1991");

        Assertions.assertEquals(mykola, obtainedUser);
    }

    @DisplayName("check getUser - assert false")
    @Test
    void getUserTestFalseAssumption() throws DBException, SQLException {
        User andriy = userDAO.getUser("Сашко", "passhash1999");
        assertNotEquals(mykola, andriy);
    }

    @DisplayName("check getUser - assert null")
    @Test
    void getUserTestNullAssumption() throws DBException, SQLException {
        User notUser = userDAO.getUser("XXXX", "YYYY");
        assertNull(notUser);
    }
}
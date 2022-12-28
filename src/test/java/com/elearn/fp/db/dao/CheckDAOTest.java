package com.elearn.fp.db.dao;

import com.elearn.fp.db.entity.Order;
import com.elearn.fp.db.entity.Unit;
import com.elearn.fp.db.entity.User;
import com.elearn.fp.db.entity.UserRole;
import com.elearn.fp.db.utils.DBManager;
import com.elearn.fp.db.utils.TestConnectionManager;
import com.elearn.fp.exception.DBException;
import com.elearn.fp.service.CheckManager;
import com.elearn.fp.service.ProductManager;
import com.elearn.fp.service.UserManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.internal.matchers.Or;

import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CheckDAOTest {

    static private Connection connection;
    private Order order;
    private CheckDAO checkDAO;
    private User mykola;
    private TestConnectionManager testConnectionManager;

    @BeforeEach
    void init() throws SQLException {
        mykola = new User();
        mykola.setRole(UserRole.SENIOR_CASHIER);
        mykola.setId(7);
        mykola.setLogin("Mykola");
        mykola.setPassword(UserManager.hashPass("1991"));

        order = new Order();
        order.setId(7);
        testConnectionManager = new TestConnectionManager();
        connection = testConnectionManager.getConnection();
        DBManager dbManager = mock(DBManager.class);
        when(dbManager.getConnection()).thenReturn(connection);

        CheckDAO.dbManager = dbManager;
        ProductDAO.dbManager = dbManager;
        checkDAO = new CheckDAO();
    }


    @Test
    void prepareChecksForView() throws DBException {
        ProductManager.unitList = new ArrayList<>();
        ProductManager.unitList.add(new Unit(1, "pcs"));
        ProductManager.unitList.add(new Unit(2, "kg"));
        String date = "2022-12-27";
        List<Order> orders = checkDAO.prepareChecksForView(mykola, 1, date);
        assertNotNull(orders);
    }

    @Test
    void getTodayChecks() throws DBException, SQLException {
        ProductManager.unitList = new ArrayList<>();
        ProductManager.unitList.add(new Unit(1, "pcs"));
        ProductManager.unitList.add(new Unit(2, "kg"));
        LocalDate localDate = LocalDate.now();
        String date = localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));


        DBManager dbManager = mock(DBManager.class);
        when(dbManager.getConnection()).thenReturn(connection);
        MockedStatic<DBManager> mockedStatic = mockStatic(DBManager.class);
        mockedStatic.when(DBManager::getInstance).thenReturn(dbManager);
        List<Order> todayList = checkDAO.prepareChecksForView(mykola, 1, date);

        connection = testConnectionManager.getConnection();
        when(dbManager.getConnection()).thenReturn(connection);

        CheckDAO.dbManager = dbManager;
        List<Order> todayListFromGetTodayCHecks = checkDAO.getTodayChecks();

        assertEquals(todayList, todayListFromGetTodayCHecks);

        mockedStatic.close();

    }

    @Test
    void getCurrentDate() throws DBException {
        LocalDate localDate = LocalDate.now();
        String date = localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        assertEquals(date, checkDAO.getCurrentDate());
    }

    @Test
    void getCurrentDateFalseAssumption() throws DBException {
        LocalDate localDate = LocalDate.of(1991, 7, 26);
        String date = localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        assertNotEquals(date, checkDAO.getCurrentDate());
    }

    @Test
    void getOrderQuantity() throws DBException, SQLException {
        ProductManager.unitList = new ArrayList<>();
        ProductManager.unitList.add(new Unit(1, "pcs"));
        ProductManager.unitList.add(new Unit(2, "kg"));
        LocalDate localDate = LocalDate.now();
        String date = localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        DBManager dbManager = mock(DBManager.class);
        connection = testConnectionManager.getConnection();
        when(dbManager.getConnection()).thenReturn(connection);
        CheckDAO.dbManager = dbManager;

        MockedStatic<DBManager> mockedStatic = mockStatic(DBManager.class);
        mockedStatic.when(DBManager::getInstance).thenReturn(dbManager);

        List<Order> todayList = checkDAO.prepareChecksForView(mykola, 1, date);
        connection = testConnectionManager.getConnection();
        when(dbManager.getConnection()).thenReturn(connection);


        assertEquals(todayList.size(), checkDAO.getOrderQuantity(mykola, date));
        mockedStatic.close();
    }
}
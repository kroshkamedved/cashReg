package com.elearn.fp.service;

import com.elearn.fp.db.dao.CheckDAO;
import com.elearn.fp.db.dao.ProductDAO;
import com.elearn.fp.db.entity.*;
import com.elearn.fp.db.utils.DBManager;
import com.elearn.fp.db.utils.TestConnectionManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CheckManagerTest {
    @Test
    void testExtractOrders() throws SQLException {
        ProductManager.unitList = new ArrayList<>();
        ProductManager.unitList.add(new Unit(1, "pcs"));
        ProductManager.unitList.add(new Unit(2, "kg"));

        ResultSet rs = mock(ResultSet.class);
        when(rs.next()).thenReturn(true).thenReturn(false);
        when(rs.getLong("o.id")).thenReturn(1l);
        when(rs.getLong("created_by")).thenReturn(1l);
        Date date = new Date();
        Timestamp timestamp = new Timestamp(date.getTime());
        when(rs.getTimestamp("date")).thenReturn(timestamp);

        MockedStatic<DBManager> mksdb = mockStatic(DBManager.class);
        DBManager dbManager = mock(DBManager.class);
        mksdb.when(DBManager::getInstance).thenReturn(dbManager);
        when(dbManager.getConnection()).thenReturn(new TestConnectionManager().getConnection());

        when(rs.getInt("product_id")).thenReturn(1);
        when(rs.getString("product_name")).thenReturn("pName");
        when(rs.getString("description")).thenReturn("pDescription");
        when(rs.getDouble("price")).thenReturn(99.0);
        when(rs.getInt("units_id")).thenReturn(1);
        when(rs.getInt("quantity")).thenReturn(10);

        List<Order> orders = CheckManager.extractOrders(rs);

        List<Order> expectedList = new ArrayList<>();
        Order expectedOrder = new Order();
        expectedOrder.setId(1);
        expectedOrder.setCashierId(1);
        expectedOrder.setDatetime(timestamp);

        Item expectedItem = new Item();
        expectedItem.setProductID(1);
        expectedItem.setProductPrice(99.0);
        expectedItem.setProductUnitId(1);
        expectedItem.setProductQuantity(10);
        expectedItem.setProductDescription("pDescription");
        expectedItem.setProductName("pName");
        List<Item> expectedItemList = new ArrayList<Item>();
        expectedItemList.add(expectedItem);
        expectedOrder.setOrderItems(expectedItemList);
        expectedList.add(expectedOrder);

        assertEquals(expectedList, orders);
        mksdb.close();
    }
}
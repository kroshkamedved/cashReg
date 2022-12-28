package com.elearn.fp.service;

import com.elearn.fp.db.entity.Item;
import com.elearn.fp.db.entity.Unit;
import com.elearn.fp.db.utils.DBManager;
import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mockStatic;

class ProductManagerTest {
    private static HttpServletRequest request;
    private static HttpServletResponse response;

    private static DBManager dbManager;
    private static ProductManager productManager;
    private static CheckManager checkManager;
    private static UserManager userManager;
    private static HttpSession session;
    private Item product;
    private ResultSet rs;
    private HttpServletRequest req;

    @BeforeEach
    void init() throws SQLException {
        product = new Item();
        product.setProductUnit("pcs");
        product.setProductUnitId(1);
        product.setProductDescription("someDescriptoin");
        product.setProductName("someProduct");
        product.setProductQuantity(99);
        product.setProductPrice(99.0);
    }

    @BeforeAll
    public static void befor() {
        ProductManager.unitList = new ArrayList<>();
        ProductManager.unitList.add(new Unit(1, "pcs"));
        ProductManager.unitList.add(new Unit(2, "kg"));
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);

    }

    @DisplayName("test extract product : assert true")
    @Test
    void testExtractProductTrueAssumption() {
        req = mock(HttpServletRequest.class);
        when(req.getParameter("prod_name")).thenReturn("someProduct");
        when(req.getParameter("description")).thenReturn("someDescriptoin");
        when(req.getParameter("prod_quantity")).thenReturn("99");
        when(req.getParameter("unit_id")).thenReturn("1");
        when(req.getParameter("product_price")).thenReturn("99.0");

        assertEquals(product, ProductManager.extractProduct(req));
    }

    @DisplayName("test extract product : assert false")
    @Test
    void testExtractProductFalseAssumption() {
        req = mock(HttpServletRequest.class);
        when(req.getParameter("prod_name")).thenReturn("anotherProduct");
        when(req.getParameter("description")).thenReturn("anothersomeDescriptoin");
        when(req.getParameter("prod_quantity")).thenReturn("99");
        when(req.getParameter("unit_id")).thenReturn("1");
        when(req.getParameter("product_price")).thenReturn("99.0");

        assertNotEquals(product, ProductManager.extractProduct(req));
    }


    @Test
    void extractItemFromGoodsTable() throws SQLException, NoSuchFieldException, IllegalAccessException {
        ResultSet rs = mock(ResultSet.class);
        when(rs.getInt("id")).thenReturn(0);
        when(rs.getString("name")).thenReturn("someProduct");
        when(rs.getString("description")).thenReturn("someDescriptoin");
        when(rs.getDouble("price")).thenReturn(99.0);
        when(rs.getInt("units_id")).thenReturn(1);
        when(rs.getInt("quantity")).thenReturn(99);

        MockedStatic<DBManager> mksdb = mockStatic(DBManager.class);
        dbManager = mock(DBManager.class);
        mksdb.when(DBManager::getInstance).thenReturn(dbManager);

        Item item = ProductManager.extractItemFromGoodsTable(rs);

        assertEquals(product, item);
        mksdb.close();
    }

}
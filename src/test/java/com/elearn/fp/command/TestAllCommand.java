package com.elearn.fp.command;

import com.elearn.fp.db.entity.Item;
import com.elearn.fp.db.entity.Unit;
import com.elearn.fp.db.entity.User;
import com.elearn.fp.db.entity.UserRole;
import com.elearn.fp.db.utils.DBManager;
import com.elearn.fp.exception.AppException;
import com.elearn.fp.service.CheckManager;
import com.elearn.fp.service.ProductManager;
import com.elearn.fp.service.UserManager;
import org.junit.BeforeClass;
import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class TestAllCommand {

    private static HttpServletRequest request;
    private static HttpServletResponse response;

    private static DBManager dbManager;
    private static ProductManager productManager;
    private static CheckManager checkManager;
    private static UserManager userManager;
    private static HttpSession session;

    private static MockedStatic<DBManager> mocketStaticDBmanager;
    private static MockedStatic<ProductManager> mockedStaticProdManager;
    private static MockedStatic<CheckManager> mockedStaticCheckManager;
    private static MockedStatic<UserManager> mockedStaticUserManager;

    @BeforeAll
    public static void init() {
        ProductManager.unitList = new ArrayList<>();
        ProductManager.unitList.add(new Unit(1, "pcs"));
        ProductManager.unitList.add(new Unit(2, "kg"));
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);

        mocketStaticDBmanager = mockStatic(DBManager.class);
        dbManager = mock(DBManager.class);
        mocketStaticDBmanager.when(DBManager::getInstance).thenReturn(dbManager);

        productManager = mock(ProductManager.class);
        mockedStaticProdManager = mockStatic(ProductManager.class);
        mockedStaticProdManager.when(ProductManager::getInstance).thenReturn(productManager);

        checkManager = mock(CheckManager.class);
        mockedStaticCheckManager = mockStatic(CheckManager.class);
        mockedStaticCheckManager.when(CheckManager::getInstance).thenReturn(checkManager);

        userManager = mock(UserManager.class);
        mockedStaticUserManager = mockStatic(UserManager.class);
        mockedStaticUserManager.when(UserManager::getInstance).thenReturn(userManager);

        session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
    }

    @AfterAll
    public static void tearDown() {
        mockedStaticCheckManager.close();
        mockedStaticProdManager.close();
        mockedStaticUserManager.close();
        mocketStaticDBmanager.close();
    }

    @DisplayName("Test addProduct command")
    @Test
    void AddProductCommandTest() throws AppException {
        String expectedPath = "cabinet/commodity_expert_page/addedItemPage";
        String commandIdentifier = "addProduct";

        doNothing().when(productManager).createProduct(request);

        Command command = CommandContainer.getCommand(commandIdentifier);
        assertEquals(expectedPath, command.execute(request, response));
    }


    @DisplayName("Test Login command for senior cashier")
    @Test
    void LoginCommandTestAdmin() throws AppException {
        String expectedPath = "cabinet/admin_page";
        String commandIdentifier = "login";

        when(request.getParameter("login")).thenReturn("login");
        when(request.getParameter("password")).thenReturn("password");


        User user = new User();
        user.setRole(UserRole.SENIOR_CASHIER);

        doNothing().when(session).setAttribute("name", null);
        when(userManager.findUser("login", "password")).thenReturn(user);

        Command command = CommandContainer.getCommand(commandIdentifier);
        assertEquals(expectedPath, command.execute(request, response));
    }

    @DisplayName("Test Login command for cashier")
    @Test
    void LoginCommandTestCashier() throws AppException {
        String expectedPath = "cabinet/cashier_page";
        String commandIdentifier = "login";

        when(request.getParameter("login")).thenReturn("login");
        when(request.getParameter("password")).thenReturn("password");


        User user = new User();
        user.setRole(UserRole.CASHIER);

        doNothing().when(session).setAttribute("name", null);
        when(userManager.findUser("login", "password")).thenReturn(user);

        Command command = CommandContainer.getCommand(commandIdentifier);
        assertEquals(expectedPath, command.execute(request, response));
    }

    @DisplayName("Test Login command for commodity expert")
    @Test
    void LoginCommandTestCommodity() throws AppException {
        String expectedPath = "cabinet/commodity_expert_page";
        String commandIdentifier = "login";

        when(request.getParameter("login")).thenReturn("login");
        when(request.getParameter("password")).thenReturn("password");


        User user = new User();
        user.setRole(UserRole.COMMODITY_EXPERT);

        doNothing().when(session).setAttribute("name", null);
        when(userManager.findUser("login", "password")).thenReturn(user);

        Command command = CommandContainer.getCommand(commandIdentifier);
        assertEquals(expectedPath, command.execute(request, response));
    }

    @DisplayName("Test add product to cart command")
    @Test
    void AddProductToCartCommandTest() throws AppException {
        String expectedPath = "cabinet/cashier_page";
        String commandIdentifier = "addProductToCart";

        doNothing().when(productManager).addProductToCart(request);

        Command command = CommandContainer.getCommand(commandIdentifier);
        assertEquals(expectedPath, command.execute(request, response));
    }

    @DisplayName("Test change language command")
    @Test
    void ChangeLanguageCommandTest() throws AppException {
        String expectedPath = "lastPage";
        String commandIdentifier = "changeLanguage";
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("lastPage")).thenReturn("lastPage");
        doNothing().when(session).setAttribute("loc", "someInfo");

        Command command = CommandContainer.getCommand(commandIdentifier);
        assertEquals(expectedPath, command.execute(request, response));
    }

    @DisplayName("Test Change Stock command")
    @Test
    void ChangeStockCommandTest() throws AppException {
        String expectedPath = "cabinet/commodity_expert_page";
        String commandIdentifier = "changeStock";

        doNothing().when(productManager).updateProductAfterPurchase(request);

        Command command = CommandContainer.getCommand(commandIdentifier);
        assertEquals(expectedPath, command.execute(request, response));
    }

    @DisplayName("Test ConfirmCheck successfully command")
    @Test
    void ConfirmCheckCommandTest() throws AppException {
        when(session.getAttribute("cart")).thenReturn(new HashMap<Item, Integer>());
        String expectedPath = "/fp/cabinet/cashier_page/check_confirmed";
        String commandIdentifier = "confirmCheck";
        when(request.getParameter("checkClosed")).thenReturn("yes");
        doNothing().when(checkManager).confirmCheck(request, null);
        doNothing().when(session).setAttribute("orders", null);

        Command command = CommandContainer.getCommand(commandIdentifier);

        assertEquals(expectedPath, command.execute(request, response));
    }

    @DisplayName("Test ConfirmCheck command with redirect to cabinet")
    @Test
    void ConfirmCheckCommandTestRedirectToCabinet() throws AppException {
        when(session.getAttribute("cart")).thenReturn(new HashMap<Item, Integer>());
        String expectedPath = "/fp/cabinet/cashier_page";
        String commandIdentifier = "confirmCheck";
        when(request.getParameter("checkClosed")).thenReturn(null);
        doNothing().when(checkManager).confirmCheck(request, null);
        doNothing().when(session).setAttribute("orders", null);

        Command command = CommandContainer.getCommand(commandIdentifier);

        assertEquals(expectedPath, command.execute(request, response));
    }

    @DisplayName("Test X report command")
    @Test
    void CreateXReportCommandTest() throws AppException {
        String expectedPath = "cabinet/admin_page/xreport?xreport=true";
        String commandIdentifier = "xReport";
        Command command = CommandContainer.getCommand(commandIdentifier);
        assertEquals(expectedPath, command.execute(request, response));
    }

    @DisplayName("Test Z report command")
    @Test
    void CreateZReportCommandTest() throws AppException {
        String expectedPath = "cabinet/admin_page/zreport";
        String commandIdentifier = "zReport";
        Command command = CommandContainer.getCommand(commandIdentifier);
        assertEquals(expectedPath, command.execute(request, response));
    }

    @DisplayName("Test DeleteItem command")
    @Test
    void CreateDeleteItemCommandTest() throws AppException {
        String expectedPath = "cabinet/commodity_expert_page";
        String commandIdentifier = "deleteItem";
        doNothing().when(productManager).deleteProduct(request);
        Command command = CommandContainer.getCommand(commandIdentifier);
        assertEquals(expectedPath, command.execute(request, response));
    }

    @DisplayName("Test Set Item Quantity Command")
    @Test
    void SetItemQuantityCommand() throws AppException {
        String expectedPath = "cabinet/cashier_page";
        String commandIdentifier = "setItemQuantity";
        when(request.getParameter("edit_goods_id")).thenReturn(null);
        Command command = CommandContainer.getCommand(commandIdentifier);
        assertEquals(expectedPath, command.execute(request, response));
    }

    @DisplayName("Test DeleteItemFromOrder command")
    @Test
    void DeleteItemFromOrderCommand() throws AppException {
        String expectedPath = "/fplastPage?checksForDate=date";
        String commandIdentifier = "deleteItemFromOrder";
        when(request.getSession().getAttribute("lastPage")).thenReturn("lastPage");
        when(request.getSession().getAttribute("date")).thenReturn("date");
        doNothing().when(productManager).deleteItemFromOrder(request);
        Command command = CommandContainer.getCommand(commandIdentifier);
        assertEquals(expectedPath, command.execute(request, response));
    }

    @DisplayName("Test DeleteWholeOrderCommand command")
    @Test
    void DeleteWholeOrderCommand() throws AppException {
        String expectedPath = "/fplastPage?checksForDate=date";
        String commandIdentifier = "deleteWholeOrder";
        when(request.getSession().getAttribute("lastPage")).thenReturn("lastPage");
        when(request.getSession().getAttribute("date")).thenReturn("date");
        doNothing().when(productManager).deleteWholeOrder(request);
        Command command = CommandContainer.getCommand(commandIdentifier);
        assertEquals(expectedPath, command.execute(request, response));
    }
}

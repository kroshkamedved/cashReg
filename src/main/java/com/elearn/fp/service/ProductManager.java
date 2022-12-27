package com.elearn.fp.service;

import com.elearn.fp.db.dao.ProductDAO;
import com.elearn.fp.db.dao.UserDAO;
import com.elearn.fp.db.entity.Item;
import com.elearn.fp.exception.AppException;
import com.elearn.fp.exception.DBException;
import com.elearn.fp.db.entity.Unit;
import com.elearn.fp.db.utils.DBManager;
import com.elearn.fp.db.utils.JdbcUtils;
import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.elearn.fp.db.query.Query.*;
import static java.lang.Long.parseLong;

import javax.servlet.http.HttpServletRequest;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class ProductManager {

    private static ProductManager instance;
    private static List<Unit> unitList;

    private Logger logger = LogManager.getLogger(ProductManager.class);

    public static synchronized ProductManager getInstance() {
        if (instance == null) {
            instance = new ProductManager();
        }
        return instance;
    }

    private ProductDAO productDAO;


    private ProductManager() {
        productDAO = new ProductDAO();
        try {
            unitList = productDAO.getUnitList();
        } catch (DBException e) {
            throw new RuntimeException(e);
        }
    }

    public static Item extractProduct(HttpServletRequest req) {
        String productName = req.getParameter("prod_name");
        String productDescription = req.getParameter("description");
        int productQuantity = Integer.parseInt(req.getParameter("prod_quantity"));
        int productUnitId = Integer.parseInt(req.getParameter("unit_id"));
        double productPrice = Double.parseDouble(req.getParameter("product_price"));
        return new Item(productName, productDescription, productQuantity, productUnitId, productPrice);
    }

    public void createProduct(HttpServletRequest request) throws DBException {
        Item product = extractProduct(request);
        productDAO.createProduct(product);
        updateAllGoods(request);
        request.getSession().setAttribute("product", product);
    }

    private void updateAllGoods(HttpServletRequest request) throws DBException {
        List<Item> allGoods = getAllGoods();
        String json = new Gson().toJson(allGoods.stream().map(item -> item.getProductName()).collect(Collectors.toList()));
        request.getServletContext().setAttribute("allGoods", json);
    }

    public void updateProductAfterPurchase(HttpServletRequest req) throws DBException {
        int newStock = Integer.parseInt(req.getParameter("newStock"));
        long productId = parseLong(req.getParameter("productId"));
        productDAO.updateProductAfterPurchase(newStock, productId);
    }

    public void updateProductAfterPurchase(long productId, int newStock) throws DBException {
        productDAO.updateProductAfterPurchase(newStock, productId);
    }

    public void deleteProduct(HttpServletRequest req) throws DBException {
        long itemId = parseLong(req.getParameter("deleteItemId"));
        productDAO.deleteProduct(itemId);
    }

    public List<Item> getAllGoods() throws DBException {
        List<Item> itemList = productDAO.getAllGoods();
        return itemList;
    }

    public static Item extractItemFromGoodsTable(ResultSet rs) throws SQLException {
        Item item = new Item();
        item.setProductID(rs.getInt("id"));
        item.setProductName(rs.getString("name"));
        item.setProductDescription(rs.getString("description"));
        item.setProductPrice(rs.getDouble("price"));
        item.setProductUnitId(rs.getInt("units_id"));
        item.setProductQuantity(rs.getInt("quantity"));
        item.setProductUnit(unitList
                .stream()
                .filter(u -> u.getId() == item.getProductUnitId())
                .findFirst()
                .get()
                .getName());
        return item;
    }

    public static Item extractItemFromOrdersItems(ResultSet rs) throws SQLException {
        Item item = new Item();
        item.setProductID(rs.getInt("product_id"));
        item.setProductName(rs.getString("product_name"));
        item.setProductDescription(rs.getString("description"));
        item.setProductPrice(rs.getDouble("price"));
        item.setProductUnitId(rs.getInt("units_id"));
        item.setProductQuantity(rs.getInt("quantity"));
        item.setProductUnit(unitList.
                stream()
                .filter(u -> u.getId() == item.getProductUnitId()).
                findFirst()
                .get()
                .getName());
        return item;
    }

    public List<Item> getGoods(HttpServletRequest req, int page, int recordsPerPage) throws DBException {
        List<Item> itemList = productDAO.getGoods(page, recordsPerPage);
        int noOfPages = productDAO.getNumberOfPage(recordsPerPage);
        req.setAttribute("noOfPages", noOfPages);
        req.setAttribute("currentPage", page);
        logger.trace("paginated goods shown");
        return itemList;
    }

    public void addProductToCart(HttpServletRequest req) throws DBException {
        String identifier = req.getParameter("prod_identifier");
        HashMap<Item, Integer> cart = (HashMap<Item, Integer>) req.getSession().getAttribute("cart");
        productDAO.addProductToCart(identifier, cart);
        logger.trace("product successfully added to cart");
    }


    public void deleteItemFromOrder(HttpServletRequest req) throws DBException {
        long deletedItemId = parseLong(req.getParameter("deleteItemId"));
        int productQuantity = Integer.parseInt(req.getParameter("productQuantity"));
        long orderId = Long.parseLong(req.getParameter("orderId"));
        productDAO.deleteItemFromOrder(orderId, deletedItemId, productQuantity);

    }

    public void deleteWholeOrder(HttpServletRequest req) throws DBException {
        long orderId = Long.parseLong(req.getParameter("deleteOrderId"));
        productDAO.deleteWholeOrder(orderId);
    }
}

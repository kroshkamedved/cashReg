package com.elearn.fp.service;

import com.elearn.fp.db.dao.CheckDAO;
import com.elearn.fp.db.entity.Item;
import com.elearn.fp.exception.DBException;
import com.elearn.fp.db.entity.Order;
import com.elearn.fp.db.entity.User;
import com.elearn.fp.db.entity.UserRole;
import com.elearn.fp.db.utils.DBManager;
import com.elearn.fp.db.utils.JdbcUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.elearn.fp.db.query.Query.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.*;
import java.util.*;
import java.util.Date;

public class CheckManager {
    private Logger logger = LogManager.getLogger(CheckManager.class);
    private static CheckManager instance;

    public static synchronized CheckManager getInstance() {
        if (instance == null) {
            instance = new CheckManager();
        }
        return instance;
    }

    private CheckDAO checkDAO;

    private CheckManager() {
        checkDAO = new CheckDAO();
    }

    /**
     * confirm check and update database
     *
     * @param request as HttpServletRequest
     * @param goods   as HashMap(item,itemQuantity)
     * @throws DBException when cannot confirm check
     */
    public void confirmCheck(HttpServletRequest request, HashMap<Item, Integer> goods) throws DBException {
        User usr = (User) request.getSession().getAttribute("usr");
        long orderId = checkDAO.confirmCheck(usr, goods);
        request.getSession().setAttribute("orderId", orderId);
    }

    public List<Order> prepareChecksForView(User usr, HttpServletRequest req, int recordsPerPage) throws DBException {
        String date = req.getParameter("checksForDate");
        if (date == null && req.getSession().getAttribute("orders") != null) {
            List orders = (List) req.getSession().getAttribute("orders");
            int ordersQuantity = orders.size();
            int noOfPages = (int) Math.ceil(ordersQuantity * 1.0 / recordsPerPage);
            req.getSession().setAttribute("noOfPages", noOfPages);
            return orders;
        }
        if (date == null) {
            date = checkDAO.getCurrentDate();
        }
        req.getSession().setAttribute("date", date);
        int noOfPages = checkDAO.getOrderQuantity(usr, date) / recordsPerPage;
        req.getSession().setAttribute("noOfPages", noOfPages);

        if (usr.getRole() == UserRole.CASHIER) {
            req.getSession().setAttribute("rolePage", "cashier_page");
        } else {
            req.getSession().setAttribute("rolePage", "admin_page");
        }
        List<Order> orders = checkDAO.prepareChecksForView(usr, recordsPerPage, date);
        return orders;
    }


    /**
     * extract orders from resulSet and return List<Order>
     *
     * @param rs
     * @return List<Order>
     * @throws SQLException
     */
    public static List<Order> extractOrders(ResultSet rs) throws SQLException {
        Map<Long, Order> orders = new HashMap<>();
        Item item;
        while (rs.next()) {
            long orderItemOrderID = rs.getLong("o.id");
            if (orders.containsKey(orderItemOrderID)) {
                item = ProductManager.getInstance().extractItemFromOrdersItems(rs);
            } else {
                Order orderItem = new Order();
                orderItem.setId(orderItemOrderID);
                orderItem.setCashierId(rs.getLong("created_by"));
                orderItem.setDatetime(rs.getTimestamp("date"));
                item = ProductManager.getInstance().extractItemFromOrdersItems(rs);
                orders.put(orderItemOrderID, orderItem);
            }
            orders.get(orderItemOrderID).getOrderItems().add(item);
        }
        List<Order> list = new ArrayList<>(orders.values());
        list.sort((a, b) -> (int) (b.getId() - a.getId()));
        return list;
    }

    /**
     * return today orders. Default method for check controller without date parameter in the request
     *
     * @return List<Order>
     * @throws DBException
     */
    public List<Order> getTodayChecks() throws DBException {
        return checkDAO.getTodayChecks();
    }

    public double calcTodaySum() throws DBException {
        return getTodayChecks()
                .stream()
                .flatMap(o->o.getOrderItems().stream())
                .mapToDouble(i->i.getProductPrice() * i.getProductQuantity())
                .sum();

    }
}

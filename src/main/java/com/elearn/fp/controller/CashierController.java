package com.elearn.fp.controller;

import com.elearn.fp.exception.DBException;
import com.elearn.fp.db.entity.ItemDTO;
import com.elearn.fp.service.CheckManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;

@WebServlet("/cabinet/cashier_page")
public class CashierController extends HttpServlet {
    Logger logger = LogManager.getLogger(CashierController.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HashMap<ItemDTO, Integer> list = (HashMap<ItemDTO, Integer>) req.getSession().getAttribute("cart");

        if (req.getParameter("edit") != null) {
            req.setAttribute("edit", Boolean.valueOf(req.getParameter("edit")));
        } else {
            req.setAttribute("edit", false);
        }
        if (req.getParameter("clear") != null) {
            list.clear();
        }
        if (req.getParameter("clear") != null) {
            list.clear();
        }
        if (req.getParameter("deleteItemId") != null) {
            int id = Integer.parseInt(req.getParameter("deleteItemId"));
            Optional<ItemDTO> first = list.keySet().stream().filter(i -> i.getProductID() == id).findFirst();
            list.remove(first.get());
            logger.trace("item with id = " + id + "deleted from list");
        }
        String url = "/WEB-INF/view/cashier_page.jsp";
        req.getServletContext().getRequestDispatcher(url).forward(req, resp);
        logger.trace("cashier controller DO GET executed");
    }
}

package com.elearn.fp.controller;

import com.elearn.fp.exception.DBException;
import com.elearn.fp.service.ProductManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/cabinet/commodity_expert_page")
public class CommodityExpertController extends HttpServlet {

    private static final Logger logger = LogManager.getLogger(CommodityExpertController.class);
    private static final int DEFAULT_RECORDS_PER_PAGE = 2;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        int recordsPerPage = DEFAULT_RECORDS_PER_PAGE;
        if (req.getParameter("page") != null) {
            int page = Integer.parseInt(req.getParameter("page"));
            req.getSession().setAttribute("page", page);
        }
        int page = (req.getSession().getAttribute("page") != null)
                ? (int) req.getSession().getAttribute("page")
                : 1;

        String url = "/WEB-INF/view/commodity_expert_page.jsp";

        ProductManager goodsManager = ProductManager.getInstance();

        if (req.getParameter("edit") != null) {
            req.setAttribute("edit", Boolean.valueOf(req.getParameter("edit")));
        } else {
            req.setAttribute("edit", false);
        }
        try {
            req.setAttribute("itemList", goodsManager.getGoods(req, page, recordsPerPage));

        } catch (DBException e) {
            req.setAttribute("ex", "cannot load product list from DB");
            logger.error("can't load goods list");
            url = "/error.jsp";
        }
        req.getServletContext().getRequestDispatcher(url).forward(req, resp);
    }
}

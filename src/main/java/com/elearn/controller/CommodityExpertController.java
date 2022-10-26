package com.elearn.controller;

import com.elearn.controller.filters.DropDownListFilter;
import com.elearn.db.DBException;
import com.elearn.db.dao.UnitDao;
import com.elearn.db.entity.ItemDTO;
import com.elearn.logic.ProductManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/cabinet/commodity_expert_page")
public class CommodityExpertController extends HttpServlet {

    private static final Logger logger = LogManager.getLogger(CommodityExpertController.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        int recordsPerPage = 2;
        if (req.getParameter("page") != null) {
            int page = Integer.parseInt(req.getParameter("page"));
            req.getSession().setAttribute("page", page);
        }
        int page = (req.getSession().getAttribute("page") != null)
                ? (int) req.getSession().getAttribute("page")
                : 1;

        String url = "/WEB-INF/view/commodity_expert_page.jsp";
        UnitDao dao = new UnitDao();
        ProductManager goodsManager = ProductManager.getInstance();

        if (req.getParameter("edit") != null) {
            req.setAttribute("edit", Boolean.valueOf(req.getParameter("edit")));
        } else {
            req.setAttribute("edit", false);
        }
        try {
            req.setAttribute("units", dao.list());
            req.setAttribute("itemDTOList", goodsManager.getGoods(req, page, recordsPerPage));
        } catch (SQLException e) {
            req.setAttribute("ex", "cannot load commodityPage, problem with units list ");
            logger.error("can't load units ");
            url = "/error.jsp";
        } catch (DBException e) {
            req.setAttribute("ex", "cannot load product list from DB");
            logger.error("can't load goods list");
            url = "/error.jsp";
        }
        req.getServletContext().getRequestDispatcher(url).forward(req, resp);
    }

}

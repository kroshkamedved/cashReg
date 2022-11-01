package com.elearn.controller;

import com.elearn.db.entity.UserRole;
import com.elearn.exception.DBException;
import com.elearn.db.entity.User;
import com.elearn.logic.CheckManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = {"/cabinet/cashier_page/checks", "/cabinet/admin_page/checks"})
public class ChecksController extends HttpServlet {

    Logger logger = LogManager.getLogger(ChecksController.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User usr = (User) req.getSession().getAttribute("usr");
        CheckManager checkManager = CheckManager.getInstance();
        String path = "/WEB-INF/view/checks.jsp";
        try {

           Object obj = req.getParameter("search");
            logger.trace(obj.toString());

            int recordsPerPage = 2;
            if (req.getParameter("page") != null) {
                int page = Integer.parseInt(req.getParameter("page"));
                req.getSession().setAttribute("page", page);
            }
            int page = (req.getSession().getAttribute("page") != null)
                    ? (int) req.getSession().getAttribute("page")
                    : 1;

            checkManager.showChecks(usr, req, page, recordsPerPage);

        } catch (DBException e) {
            path = req.getServletContext().getAttribute("app") + "/error.jsp";
            logger.error("ChecksController doGet() forwarded to ==> " + path);
        }
        req.getRequestDispatcher(path).forward(req, resp);
        logger.trace("checksController - doGet done");
    }
}

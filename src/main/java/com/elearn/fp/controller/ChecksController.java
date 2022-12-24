package com.elearn.fp.controller;

import com.elearn.fp.exception.DBException;
import com.elearn.fp.db.entity.User;
import com.elearn.fp.service.CheckManager;
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
        logger.info(req.getQueryString());
        User usr = (User) req.getSession().getAttribute("usr");
        CheckManager checkManager = CheckManager.getInstance();
        String path = "/WEB-INF/view/checks.jsp";
        int recordsPerPage = 1;
        try {
            if (req.getParameter("recordsPerPage") != null) {
                recordsPerPage = Integer.parseInt(req.getParameter("recordsPerPage"));
                req.getSession().setAttribute("recordsPerPage", recordsPerPage);
            } else if (req.getSession().getAttribute("recordsPerPage") == null) {
                req.getSession().setAttribute("recordsPerPage", recordsPerPage);
            } else {
                recordsPerPage = (int) req.getSession().getAttribute("recordsPerPage");
            }

            if (req.getParameter("page") != null) {
                int page = Integer.parseInt(req.getParameter("page"));
                req.setAttribute("page", page);
            } else {
                int page = (req.getSession().getAttribute("page") != null)
                        ? (int) req.getSession().getAttribute("page")
                        : 1;
                req.setAttribute("page", page);
                checkManager.prepareChecksForView(usr, req, page, recordsPerPage);
            }
        } catch (DBException e) {
            path = req.getServletContext().getAttribute("app") + "/error.jsp";
            logger.error("ChecksController doGet() forwarded to ==> " + path);
        }
        req.getRequestDispatcher(path).forward(req, resp);
        logger.trace("checksController - doGet done");
    }
}

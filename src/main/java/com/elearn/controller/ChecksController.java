package com.elearn.controller;

import com.elearn.exception.DBException;
import com.elearn.db.entity.User;
import com.elearn.logic.CheckManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = {"/cabinet/cashier_page/checks", "/cabinet/admin_page/checks"})
public class ChecksController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User usr = (User) req.getSession().getAttribute("usr");
        CheckManager checkManager = CheckManager.getInstance();
        String path = "/WEB-INF/view/checks.jsp";
        try {
            checkManager.showChecks(usr, req);
        } catch (DBException e) {
            path = "error.jsp";
        }
        req.getRequestDispatcher(path).forward(req, resp);
    }
}

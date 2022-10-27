package com.elearn.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/cabinet/cashier_page")
public class CashierController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getParameter("edit") != null) {
            req.setAttribute("edit", Boolean.valueOf(req.getParameter("edit")));
        } else {
            req.setAttribute("edit", false);
        }

        String url = "/WEB-INF/view/cashier_page.jsp";
        req.getServletContext().getRequestDispatcher(url).forward(req, resp);
    }
}

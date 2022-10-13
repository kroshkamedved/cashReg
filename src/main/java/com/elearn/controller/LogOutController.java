package com.elearn.controller;

import com.elearn.controller.filters.RoleFilter;
import com.elearn.db.entity.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/logout")
public class LogOutController extends HttpServlet {


    private static final Logger logger = LogManager.getLogger(LogOutController.class);
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User usr = (User) req.getSession().getAttribute("usr");

        req.getSession().invalidate();
        logger.info("session invalidated for user :" + usr.getLogin() + " " + usr.getRole());
        req.getRequestDispatcher("index.jsp").forward(req,resp);
    }
}

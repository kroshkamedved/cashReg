package com.elearn.fp.controller;

import com.elearn.fp.db.entity.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Logout servlet
 */
@WebServlet("/logout")
public class LogOutController extends HttpServlet {


    private static final Logger logger = LogManager.getLogger(LogOutController.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User usr = (User) req.getSession().getAttribute("usr");

        req.getSession().invalidate();
        logger.info("session invalidated for user :" + usr.getLogin() + " " + usr.getRole());
        resp.sendRedirect("index.jsp");
    }
}

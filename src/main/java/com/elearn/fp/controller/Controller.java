package com.elearn.fp.controller;

import com.elearn.fp.command.CommandContainer;
import com.elearn.fp.command.Command;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Main controller. Choose and execute appropriate command
 */
@WebServlet("/controller")
public class Controller extends HttpServlet {
    private static final Logger logger = LogManager.getLogger(Controller.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String address = "error.jsp";

        String commandName = req.getParameter("command");
        logger.trace("commandNameFromLogger ==> " + commandName);

        Command command = CommandContainer.getCommand(commandName);
        try {
            address = command.execute(req, resp);
        } catch (Exception ex) {
            logger.error("can't execute " + commandName);
            req.setAttribute("ex", ex);
        }
        logger.trace("address ==> " + address);
        req.getRequestDispatcher(address).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String address = "error.jsp";
        String commandName = req.getParameter("command");
        logger.trace("doPostControllerCommand ==> " + commandName);

        Command command = CommandContainer.getCommand(commandName);
        try {
            address = command.execute(req, resp);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            req.getSession().setAttribute("ex", "cannot do " + commandName);
        }
        logger.trace("address ==> " + address);
        resp.sendRedirect(address);
    }
}

package com.elearn.controller;
import com.elearn.command.CommandContainer;
import com.elearn.command.Command;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/controller")
public class Controller  extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String address = "error.jsp";
        String commandName = req.getParameter("command");
        System.out.println("commandName ==> " + commandName); //trace

        Command command = CommandContainer.getCommand(commandName);
        try {
             address = command.execute(req,resp);
        } catch (Exception ex){
            //Log4j
            req.setAttribute("ex",ex);
        }
        System.out.println("address ==> " + address);
        req.getRequestDispatcher(address).forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String address = "error.jsp";
        String commandName = req.getParameter("command");
        System.out.println("commandName ==> " + commandName); //trace

        Command command = CommandContainer.getCommand(commandName);
        try {
            address = command.execute(req,resp);
        } catch (Exception ex){
            //Log4j
            req.getSession().setAttribute("ex",ex);
        }
        System.out.println("address ==> " + address);
        resp.sendRedirect(address);
    }
}

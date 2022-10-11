package com.elearn.controller.filters;

import javax.naming.Context;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.ContentHandler;
import java.util.logging.Logger;

//@WebFilter( "/")
public class AuthenticationFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("filter ==> doFilter");
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;

        HttpSession session = req.getSession();

        if(session.getAttribute("usr") != null){
            System.out.printf("==> filter find usr");
            resp.sendRedirect("admin_page");
            return;
        }
        
        filterChain.doFilter(servletRequest,servletResponse);
    }
}

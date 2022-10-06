package com.elearn.controller.filters;

import com.elearn.db.entity.UserRole;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RoleFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        switch ((UserRole) request.getSession().getAttribute("role")) {
            case SENIOR_CASHIER:
                response.sendRedirect("admin_page");
                break;
            case COMMODITY_EXPERT:
                response.sendRedirect("commodity_expert_page");
                break;
            case CASHIER:
                response.sendRedirect("cashier_page");
                break;
        }
    }
}

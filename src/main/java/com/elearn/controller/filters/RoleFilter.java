package com.elearn.controller.filters;

import com.elearn.controller.Controller;
import com.elearn.db.entity.UserRole;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.management.relation.Role;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("/index.jsp")
public class RoleFilter implements Filter {

    private static final Logger logger = LogManager.getLogger(RoleFilter.class);

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String uri = request.getRequestURI();
        HttpSession session = request.getSession();
        UserRole role = (UserRole) session.getAttribute("role");

        if (role != null && "/fp/".equals(uri)) {
            switch (role) {
                case SENIOR_CASHIER:

                    response.sendRedirect("cabinet/admin_page");
                    logger.trace("filtered: admin session");
                    break;
                case COMMODITY_EXPERT:
                    response.sendRedirect("cabinet/commodity_expert_page");
                    logger.trace("filtered: commodity expert session");
                    break;
                case CASHIER:
                    response.sendRedirect("cabinet/cashier_page");
                    logger.trace("filtered: cashier expert session");
                    break;
            }
        }
        filterChain.doFilter(request, response);
    }
}


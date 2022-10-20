package com.elearn.controller.filters;

import com.elearn.db.entity.User;
import com.elearn.db.entity.UserRole;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@WebFilter("/cabinet/*")
public class AuthorizationFilter implements Filter {
    Logger logger = LogManager.getLogger(AuthorizationFilter.class);

    Map<String, UserRole> pages = new HashMap<>();

    @Override

    public void init(FilterConfig filterConfig) throws ServletException {
        pages.put("admin_page", UserRole.SENIOR_CASHIER);
        pages.put("commodity_expert_page", UserRole.COMMODITY_EXPERT);
        pages.put("cashier_page", UserRole.CASHIER);
    }


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String forward = "/fp/";

        User usr = (User) request.getSession().getAttribute("usr");

        if (usr != null && usr.getClass() == (User.class)) {
            String uri = request.getRequestURI();
            Optional<String> key = pages.keySet().stream()
                    .filter(s -> uri.contains(s))
                    .findFirst();
            String usrKey = key.get();

            if (usrKey != null && usr.getRole() == pages.get(usrKey)) {
                filterChain.doFilter(request, response);
                logger.trace("user: " + usr.getLogin() + " moved to " + uri);
                return;
            }
        }
        filterChain.doFilter(request, response);
        response.sendRedirect(forward);
        logger.info("access forbidden, user " + usr.getLogin() + " was redirected to the cabinet");
    }
}

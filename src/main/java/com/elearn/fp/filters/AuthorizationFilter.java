package com.elearn.fp.filters;

import com.elearn.fp.db.entity.User;
import com.elearn.fp.db.entity.UserRole;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * WebFilter which filter unauthorized access. Identifies the user and determines access rights.
 */
@WebFilter(value = "/cabinet/*", dispatcherTypes = {DispatcherType.REQUEST, DispatcherType.FORWARD})
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
            String usrKey = key.orElse(null);

            if (usrKey != null && usr.getRole() == pages.get(usrKey)) {
                filterChain.doFilter(request, response);
                logger.trace("user: " + usr.getLogin() + " moved to " + uri);
                return;
            }
        }
        String userLogin = (usr != null) ? usr.getLogin() : "guest";
        response.sendRedirect(forward);
        logger.info("access forbidden, user "+ userLogin +" tried to access: " +request.getRequestURI() + " was redirected to the cabinet");
    }
}

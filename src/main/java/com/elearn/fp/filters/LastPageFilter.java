package com.elearn.fp.filters;


import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@WebFilter(value = "/*", dispatcherTypes = {DispatcherType.REQUEST, DispatcherType.FORWARD})
public class LastPageFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        if (pageIsNotValid(req)) {
            req.getSession().setAttribute("lastPage", req.getServletPath());
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private boolean pageIsNotValid(HttpServletRequest req) {
       return  !(req.getRequestURI().contains("controller")
               ||
               req.getRequestURI().contains("admin_page/zreport")
               ||
               req.getRequestURI().contains("WEB-INF"));
    }
}

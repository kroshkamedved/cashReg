package com.elearn.fp.filters;


import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Filter which hold in session last relevant page.
 */
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

    /**
     * checks whether page is relevant for storing as "return page" or not.
     * "return page" is the page to which user would be returned
     * after some actions were performed which shouldn't affect previous view.
     * @param req
     * @return
     */
    private boolean pageIsNotValid(HttpServletRequest req) {
       return  !(req.getRequestURI().contains("controller")
               ||
               req.getRequestURI().contains("admin_page/zreport")
               ||
               req.getRequestURI().contains("WEB-INF"));
    }
}

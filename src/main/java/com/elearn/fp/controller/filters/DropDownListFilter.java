package com.elearn.fp.controller.filters;

import com.elearn.fp.exception.DBException;
import com.elearn.fp.logic.ProductManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

@WebFilter("/cabinet/commodity_expert_page")
public class DropDownListFilter implements Filter {
    private static final Logger logger = LogManager.getLogger(DropDownListFilter.class);

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            servletRequest.setAttribute("units", ProductManager.getInstance().getUnitList());
        } catch (DBException e) {
            servletRequest.setAttribute("ex", "cannot load units list from DB");
            logger.error("can't load units list");
            servletRequest.getServletContext().getRequestDispatcher("error.jsp").forward(servletRequest, servletResponse);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}

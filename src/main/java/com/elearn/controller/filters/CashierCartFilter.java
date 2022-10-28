package com.elearn.controller.filters;

import com.elearn.db.entity.ItemDTO;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

@WebFilter(urlPatterns = {"/cabinet/cashier_page/", "/cabinet/cashier_page/*"})
public class CashierCartFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpSession session = req.getSession();
        request.setCharacterEncoding("UTF-8");
        if (session.getAttribute("cart") == null) {
            //  session.setAttribute("cart", new ArrayList<ItemDTO>());
            session.setAttribute("cart", new HashMap<ItemDTO, Integer>());
        }
        chain.doFilter(request, response);
    }
}
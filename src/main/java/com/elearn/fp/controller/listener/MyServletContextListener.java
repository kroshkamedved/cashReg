package com.elearn.fp.controller.listener;

import com.elearn.fp.exception.DBException;
import com.elearn.fp.service.ProductManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class MyServletContextListener implements ServletContextListener {
    Logger contextLogger = LogManager.getLogger(MyServletContextListener.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext sc = sce.getServletContext();
        String path = sc.getContextPath();
        sc.setAttribute("app", path);
        sc.setAttribute("contextLogger", contextLogger);
        try {
            sc.setAttribute("units", ProductManager.getInstance().getUnitList());
            contextLogger.trace("Context-listener successfully LOADED");
        } catch (DBException e) {
            contextLogger.error("cannot load units from db in context initializing block", e);
        }
    }
}

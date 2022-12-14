package com.elearn.fp.listener;

import com.elearn.fp.db.dao.ProductDAO;
import com.elearn.fp.exception.DBException;
import com.elearn.fp.service.ProductManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * ServletContextListener
 * set three context attributes:
 * app - String with app path,
 * contextLogger(self-described name),
 * units - List<Unit> with units of store products
 */
public class MyServletContextListener implements ServletContextListener {
    Logger contextLogger = LogManager.getLogger(MyServletContextListener.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext sc = sce.getServletContext();
        String path = sc.getContextPath();
        sc.setAttribute("app", path);
        sc.setAttribute("contextLogger", contextLogger);
        try {
            sc.setAttribute("units", new ProductDAO().getUnitList());
            contextLogger.trace("Context-listener successfully LOADED");
        } catch (DBException e) {
            contextLogger.error("cannot load units from db in context initializing block", e);
        }
    }
}

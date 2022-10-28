package com.elearn.controller.listener;

import com.elearn.db.DBException;
import com.elearn.logic.ProductManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.sql.SQLException;

public class MyServletContextListener implements ServletContextListener {
    Logger contextLogger = LogManager.getLogger(MyServletContextListener.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext sc = sce.getServletContext();
        sc.setAttribute("contextLogger", contextLogger);
        try {
            sc.setAttribute("units", ProductManager.getInstance().getUnitList());
            contextLogger.trace("listener successfully LOADED");
        } catch (SQLException e) {
            contextLogger.error("cannot load units from db in context initializing block", e);
        }
    }
}

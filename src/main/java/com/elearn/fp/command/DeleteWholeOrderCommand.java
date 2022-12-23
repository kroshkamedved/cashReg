package com.elearn.fp.command;

import com.elearn.fp.exception.AppException;
import com.elearn.fp.exception.DBException;
import com.elearn.fp.service.ProductManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Command for deleting from db existing order
 */
public class DeleteWholeOrderCommand implements Command {
    Logger logger = LogManager.getLogger(DeleteWholeOrderCommand.class);
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws AppException {
        ProductManager pm = ProductManager.getInstance();
        try {
            pm.deleteWholeOrder(req);
            logger.info("order deleted");
        } catch (DBException e) {
            throw new AppException(e.getMessage(), e);
        }
        return "/fp" + ((String) req.getSession().getAttribute("lastPage")) + "?checksForDate=" + req.getSession()
                .getAttribute("date");
    }
}

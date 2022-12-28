package com.elearn.fp.command;

import com.elearn.fp.exception.AppException;
import com.elearn.fp.exception.DBException;
import com.elearn.fp.service.ProductManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Command for order editing.Allows cashier to delete item from current shopping cart
 */
public class DeleteItemFromOrderCommand implements Command {

    Logger logger  = LogManager.getLogger(DeleteItemFromOrderCommand.class);
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws AppException {
        ProductManager pm = ProductManager.getInstance();
        try {
            pm.deleteItemFromOrder(req);
            logger.info("item deleted from order");
        } catch (DBException e) {
            throw new AppException(e.getMessage(), e);
        }
        return "/fp" + (req.getSession().getAttribute("lastPage")) + "?checksForDate=" + req.getSession()
                .getAttribute("date");
    }
}

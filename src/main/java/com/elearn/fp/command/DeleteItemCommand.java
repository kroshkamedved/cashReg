package com.elearn.fp.command;

import com.elearn.fp.exception.AppException;
import com.elearn.fp.service.ProductManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DeleteItemCommand implements Command {
    Logger logger = LogManager.getLogger(DeleteItemCommand.class);

    /**
     * Delete item from stock command.
     * @param req
     * @param resp
     * @return URI of the commodity expert page
     * @throws AppException
     */
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws AppException {
        ProductManager pm = ProductManager.getInstance();
        pm.deleteProduct(req);
        logger.trace("product deleted");
        return "cabinet/commodity_expert_page";
    }
}

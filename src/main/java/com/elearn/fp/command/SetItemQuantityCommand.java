package com.elearn.fp.command;

import com.elearn.fp.db.entity.Item;
import com.elearn.fp.exception.AppException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Optional;

/**
 * Command for assigning quantity of item in the shopping cart
 */
public class SetItemQuantityCommand implements Command {

    Logger logger = LogManager.getLogger(SetItemQuantityCommand.class);
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws AppException {
        HashMap<Item, Integer> list = (HashMap<Item, Integer>) req.getSession().getAttribute("cart");
        if (req.getParameter("edit_goods_id") != null) {
            Integer productId = Integer.parseInt(req.getParameter("edit_goods_id"));
            Optional<Item> itemDTO = list.keySet().stream().filter(k -> k.getProductID() == productId).findFirst();
            Integer unitQuant = Integer.parseInt(req.getParameter("unit_quantity"));
            list.replace(itemDTO.get(), unitQuant);
            logger.info("item :" + productId + " quantity changed ");
        }
        return "cabinet/cashier_page";
    }
}

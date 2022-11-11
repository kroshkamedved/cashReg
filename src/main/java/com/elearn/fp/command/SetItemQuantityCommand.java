package com.elearn.fp.command;

import com.elearn.fp.db.entity.ItemDTO;
import com.elearn.fp.exception.AppException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Optional;

public class SetItemQuantityCommand implements Command {
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws AppException {
        HashMap<ItemDTO, Integer> list = (HashMap<ItemDTO, Integer>) req.getSession().getAttribute("cart");
        if (req.getParameter("edit_goods_id") != null) {
            Integer productId = Integer.parseInt(req.getParameter("edit_goods_id"));
            Optional<ItemDTO> itemDTO = list.keySet().stream().filter(k -> k.getProductID() == productId).findFirst();

            Integer unitQuant = Integer.parseInt(req.getParameter("unit_quantity"));
            list.replace(itemDTO.get(), unitQuant);
        }
        return "cabinet/cashier_page";
    }
}

package com.elearn.fp.command;

import com.elearn.fp.db.entity.ItemDTO;
import com.elearn.fp.exception.AppException;
import com.elearn.fp.exception.DBException;
import com.elearn.fp.service.CheckManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

public class ConfirmCheckCommand implements Command {

    Logger logger = LogManager.getLogger(ConfirmCheckCommand.class);

    /**
     * confirm order and save it to db.
     * @param req
     * @param resp
     * @return cashier main page uri
     * @throws AppException
     */
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws AppException {
        HashMap<ItemDTO, Integer> list = (HashMap<ItemDTO, Integer>) req.getSession().getAttribute("cart");
        if (req.getParameter("checkClosed") != null) {
            try {
                CheckManager.getInstance().confirmCheck(req, list);
                list.clear();
                logger.trace("check confirmed. Changes saved to db");
                return "/fp/cabinet/cashier_page/check_confirmed";
            } catch (DBException e) {
                logger.error("cannot confirm check");
                throw new AppException("cannot confirm check", e);
            }
        }
        return "/fp/cabinet/cashier_page";
    }
}

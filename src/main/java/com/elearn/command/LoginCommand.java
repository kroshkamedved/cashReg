package com.elearn.command;

import com.elearn.db.DBException;
import com.elearn.db.DBManager;
import com.elearn.db.entity.User;
import com.elearn.db.entity.UserRole;
import com.elearn.logic.UserManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginCommand implements Command {
    static final Logger logger = LogManager.getLogger(LoginCommand.class);

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws DBException {
        String login = req.getParameter("login");
        logger.info("login ==> " + login);

        String password = req.getParameter("password");
        User user = UserManager.getInstance().findUser(login, password);
        UserRole currentRole = user.getRole();

        logger.info("user logged in : " + user.getLogin() + " user role : " + currentRole);

        req.getSession().setAttribute("role", currentRole);
        req.getSession().setAttribute("usr", user);

        String result = null;

        switch (currentRole) {
            case SENIOR_CASHIER:
                result = "cabinet/admin_page.jsp";
                break;
            case COMMODITY_EXPERT:
                result = "cabinet/commodity_expert_page";
                break;
            case CASHIER:
                result = "cabinet/cashier_page";
                break;
        }
        return result;
    }
}

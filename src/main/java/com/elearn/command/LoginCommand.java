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

        //if user is logged ==> req.getSession().setAttribute("currentUSer", user);
        logger.info("user name ==> " + user.getLogin());
        UserRole currentRole = user.getRole();

        req.getSession().setAttribute("role", currentRole);
        req.getSession().setAttribute("usr", user);

        String result = null;

        switch (currentRole) {
            case SENIOR_CASHIER:
                result = "admin_page.jsp";
                break;
            case COMMODITY_EXPERT:
                result = "commodity_expert_page";
                break;
            case CASHIER:
                result = "cashier_page";
                break;
        }
        return result;
    }
}

package com.elearn.fp.command;

import com.elearn.fp.exception.DBException;
import com.elearn.fp.db.entity.User;
import com.elearn.fp.db.entity.UserRole;
import com.elearn.fp.service.UserManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

/**
 * Login commands holds the map with different user types. Checks the user role and redirect to the corresponding cabinet.
 */
public class LoginCommand implements Command {
    static final Logger logger = LogManager.getLogger(LoginCommand.class);
    private HashMap<UserRole, String> cabinetsMap;

    public LoginCommand() {
        initContext();
    }


    @Override
    public void initContext() {
        cabinetsMap = new HashMap<UserRole, String>();
        cabinetsMap.put(UserRole.SENIOR_CASHIER, "cabinet/admin_page");
        cabinetsMap.put(UserRole.COMMODITY_EXPERT, "cabinet/commodity_expert_page");
        cabinetsMap.put(UserRole.CASHIER, "cabinet/cashier_page");
    }

    /**
     * Checks the user data correctness and redirect to the corresponding cabinet.
     * @param req
     * @param resp
     * @return
     * @throws DBException
     */
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws DBException {
        String login = req.getParameter("login");
        String password = req.getParameter("password");

        User user = UserManager.getInstance().findUser(login, password);
        UserRole currentRole = user.getRole();

        logger.info("user logged in : " + user.getLogin() + " user role : " + currentRole);

        req.getSession().setAttribute("role", currentRole);
        req.getSession().setAttribute("usr", user);

        return cabinetsMap.get(currentRole);
    }
}

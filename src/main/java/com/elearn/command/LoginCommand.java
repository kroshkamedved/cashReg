package com.elearn.command;

import com.elearn.db.DBException;
import com.elearn.db.DBManager;
import com.elearn.db.entity.User;
import com.elearn.db.entity.UserRole;
import com.elearn.logic.UserManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginCommand implements Command {

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws DBException {
        String login = req.getParameter("login");
        System.out.println("login ==> " + login);

        String password = req.getParameter("password");

        User user = UserManager.getInstance().findUser(login, password);

        //if user is logged ==> req.getSession().setAttribute("currentUSer", user);
        System.out.println("user name ==> " + user.getLogin());

        req.setAttribute("role", user.getRole());
        req.setAttribute("usr",user);

        if (UserRole.SENIOR_CASHIER == user.getRole()) {
            return "admin_page.jsp";
        }
        if ("client".equals(login)) {
            return "client_page.jsp";
        }
        return null;
    }
}

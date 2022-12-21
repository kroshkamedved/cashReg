package com.elearn.fp.command;

import com.elearn.fp.exception.AppException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ChangeLanguageCommand implements Command {

    Logger logger = LogManager.getLogger(ChangeLanguageCommand.class);

    /**
     * change interface language
     * @param req
     * @param resp
     * @return last visited servlet uri
     * @throws AppException
     */
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws AppException {
        String loc = req.getParameter("loc");
        req.getSession().setAttribute("loc", loc);
        logger.trace("language changed");
        return ((String) req.getSession().getAttribute("lastPage"));
    }
}

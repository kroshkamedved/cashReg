package com.elearn.fp.command;

import com.elearn.fp.exception.AppException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CreateXReportCommand implements Command {
    Logger logger = LogManager.getLogger(CreateXReportCommand.class);

    /**
     * Command forward the user to the ReportController
     *
     * @param req
     * @param resp
     * @return URI of the ReportController servlet.
     * @throws AppException
     */

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws AppException {
        logger.trace("going to generate x report");
        return "cabinet/admin_page/xreport?xreport=true";
    }
}

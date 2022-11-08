package com.elearn.fp.command;

import com.elearn.fp.db.entity.Order;
import com.elearn.fp.exception.AppException;
import com.elearn.fp.service.CheckManager;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.Filter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class CreateZReportCommand implements Command {
    Logger logger = LogManager.getLogger(CreateZReportCommand.class);

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws AppException {
        return "cabinet/admin_page/zreport";
    }
}

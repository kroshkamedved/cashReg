package com.elearn.fp.controller;

import com.elearn.fp.db.entity.Order;
import com.elearn.fp.exception.AppException;
import com.elearn.fp.exception.DBException;
import com.elearn.fp.service.CheckManager;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.time.LocalDate;
import java.util.List;

@WebServlet("/cabinet/admin_page/zreport")
public class ReportController extends HttpServlet {
    Logger logger = LogManager.getLogger(ReportController.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Document document = new Document();
        try {
            File report = new File("c:/projTmp/" + "z_report_" + LocalDate.now() + ".pdf");
            report.getParentFile().mkdirs();
            report.createNewFile();
            PdfWriter.getInstance(document, new FileOutputStream(report));
            document.open();
            List<Order> todayOrders = CheckManager.getInstance().getTodayChecks(req);

            Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);

            Chunk chunk = new Chunk("sorry, but there are no sales for today");
            chunk.setFont(font);

            com.itextpdf.text.List list = new com.itextpdf.text.List(true, 15);

            for (Order order :
                    todayOrders) {
                list.add(new ListItem(order.toString()));
            }
            if (list.isEmpty()) {
                document.add(chunk);
            }

            document.add(list);
            document.close();

            resp.setContentType("application/pdf");
            resp.addHeader("Content-Disposition", "attachment; filename=" + report.getName());
            resp.setContentLength((int) report.length());

            FileInputStream fileInputStream = new FileInputStream(report);
            OutputStream responseOutputStream = resp.getOutputStream();
            int bytes;
            while ((bytes = fileInputStream.read()) != -1) {
                responseOutputStream.write(bytes);
            }
        } catch (DocumentException e) {
            logger.error("file cannot be created");
        } catch (FileNotFoundException e) {
            logger.error("file cannot be created");
        } catch (IOException e) {
            logger.error("file cannot be created");
        } catch (DBException e) {
            logger.error("file cannot be created");
        }
    }
}

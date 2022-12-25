package com.elearn.fp.controller;

import com.elearn.fp.db.entity.Item;
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
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

/**
 * Servlet for report generation
 */
@WebServlet({"/cabinet/admin_page/zreport", "/cabinet/admin_page/xreport"})
public class ReportController extends HttpServlet {
    Logger logger = LogManager.getLogger(ReportController.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        boolean isZreport = true;
        Document document = new Document();
        try {
            if (req.getParameter("xreport") != null) {
                isZreport = false;
            }
            File report = new File("projTmp/" + "report_" + LocalDate.now() + ".pdf");
            report.getParentFile().mkdirs();
            report.createNewFile();
            PdfWriter.getInstance(document, new FileOutputStream(report));
            document.open();
            List<Order> todayOrders = CheckManager.getInstance().getTodayChecks(req);

            Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);

            Chunk chunk = new Chunk("sorry, but there are no sales for today");
            chunk.setFont(font);

            com.itextpdf.text.List list = new com.itextpdf.text.List(true, 15);

            if (!todayOrders.isEmpty()) {
                double sum = todayOrders.stream()
                        .flatMap(s -> s.getOrderItems().stream())
                        .mapToDouble(s -> s.getProductQuantity() * s.getProductPrice())
                        .sum();

                String totalIncome = String.format("Total income for %tF is %,.2f$", LocalDate.now(), sum);
                chunk = new Chunk(totalIncome);

                for (Order order : todayOrders) {
                    list.add(new ListItem(order.toString()));
                    com.itextpdf.text.List itemList = new com.itextpdf.text.List(true, 10);
                    for (Item item : order.getOrderItems()) {
                        itemList.add(item.toString());
                    }
                    double checkTotal = order.getOrderItems()
                            .stream()
                            .mapToDouble(s -> s.getProductPrice() * s.getProductQuantity())
                            .sum();
                    itemList.setNumbered(false);
                    itemList.add("TOTAL CHECK SUM : " + checkTotal);
                    list.add(itemList);
                }
            }
            document.add(chunk);
            document.add(list);
            document.close();

            resp.setContentType("application/pdf");
            if (isZreport) {
                resp.addHeader("Content-Disposition", "attachment; filename=" + report.getName());
            }
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

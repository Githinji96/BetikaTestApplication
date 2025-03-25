package com.utils;


import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.ExtentReports;
import com.itextpdf.html2pdf.HtmlConverter;


import java.io.*;

public class ExtentReportManager {
        private static ExtentReports extent;

        public static ExtentReports getReportInstance() {
            if (extent == null) {
                String reportPath = System.getProperty("user.dir") + "/reports/ExtentReport.html";
                ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
                sparkReporter.config().setReportName("Betika App Automation Test Report");
                sparkReporter.config().setDocumentTitle("Test Execution Report");

                extent = new ExtentReports();
                extent.attachReporter(sparkReporter);
                extent.setSystemInfo("Tester", "Bonface Githinji");
                extent.setSystemInfo("Environment", "QA");
            }
            return extent;
        }
        public static void convertHtmlToPdf() {
            String htmlFilePath = System.getProperty("user.dir") + "/reports/ExtentReport.html";
            String pdfFilePath = System.getProperty("user.dir") + "/reports/ExtentReport.pdf";

            try {
                File htmlFile = new File(htmlFilePath);
                File pdfFile = new File(pdfFilePath);

                if (!htmlFile.exists()) {
                    System.out.println("ExtentReport.html not found!");
                    return;
                }

                // Convert HTML to PDF
                HtmlConverter.convertToPdf(new FileInputStream(htmlFile), new FileOutputStream(pdfFile));
              //  System.out.println("PDF Report Generated: " + pdfFilePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



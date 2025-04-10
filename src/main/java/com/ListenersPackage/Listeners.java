package com.ListenersPackage;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.utils.ExtentReportManager;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import com.utils.DriverClass;
import java.util.Date;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;


public class Listeners implements ITestListener {
    private static ExtentReports extent = ExtentReportManager.getReportInstance();
    private static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();
    private DriverClass driverClass = new DriverClass();  // Instance of DriverClass

    @Override
    public void onTestStart(ITestResult result) {
        ExtentTest test = extent.createTest(result.getMethod().getMethodName());
        extentTest.set(test);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        extentTest.get().log(Status.PASS, "Test Passed: " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
//        extentTest.get().log(Status.FAIL, "Test Failed: " + result.getMethod().getMethodName());
//        extentTest.get().log(Status.FAIL, result.getThrowable().getMessage());
//
//        ExtentTest test = extentTest.get();
//        test.log(Status.FAIL, "Test Failed: " + result.getMethod().getMethodName());
//        test.log(Status.FAIL, result.getThrowable().getMessage());
//
        ExtentTest test = extentTest.get();

        // Log test failure details
        test.log(Status.FAIL, "Test Failed: " + result.getMethod().getMethodName());
        test.log(Status.FAIL, result.getThrowable());

        try {
            WebDriver driver = driverClass.getDriver();
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String screenshotName = result.getMethod().getMethodName() + "_" + timestamp + ".png";

            // Take screenshot and get the path
            String screenshotPath = driverClass.takeScreenshot(screenshotName);

            // Attach screenshot to report
            if (screenshotPath != null) {
                // Use the same path format as takeScreenshot method
                test.fail("Screenshot on Failure",
                        MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
            } else {
                test.fail("Failed to capture screenshot");
            }
        } catch (Exception e) {
            test.fail("Failed during failure handling: " + e.getMessage());
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        extentTest.get().log(Status.SKIP, "Test Skipped: " + result.getMethod().getMethodName());
    }

    @Override
    public void onFinish(ITestContext context) {
        extent.flush(); // Write report to file
    }
}

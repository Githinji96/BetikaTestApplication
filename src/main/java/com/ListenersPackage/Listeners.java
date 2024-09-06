package com.ListenersPackage;

import com.utils.DriverClass;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.IOException;

public class Listeners extends DriverClass implements ITestListener {
    DriverClass scrn;
    public void onTestStart(ITestResult result) {
      System.out.println("Test Case execution is starting");
    }

    // This method is invoked when a test case succeeds.
    @Override
    public void onTestSuccess(ITestResult result) {

    }

    // This method is invoked when a test case fails.
    @Override
    public void onTestFailure(ITestResult result) {

        System.out.println("Test case failed");
        try {
            takeScreenshot(result.getMethod().getMethodName()+" .jpg");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    // This method is invoked when a test case is skipped.
    @Override
    public void onTestSkipped(ITestResult result) {

    }

    // This method is invoked when a test case fails but within the success percentage.
    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {

    }

    // This method is invoked when a test case fails and is marked as a "failed" retry.
   @Override
    public void onTestFailedWithTimeout(ITestResult result) {

    }

    // This method is invoked before the start of any test cases in a test tag in a testng.xml file.
    @Override
    public void onStart(ITestContext context) {

    }

    // This method is invoked after all the test cases in a test tag in a testng.xml file have been run.
    @Override
    public void onFinish(ITestContext context) {

    }
}

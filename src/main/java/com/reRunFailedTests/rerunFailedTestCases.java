package com.reRunFailedTests;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class rerunFailedTestCases  implements IRetryAnalyzer {
    private int retryCount = 0;
    private static final int maxRetryCount = 4; // Set the maximum retry count

    @Override
    public boolean retry(ITestResult result) {
        if (retryCount < maxRetryCount) {
            retryCount++;
            return true; // Return true if you want to retry
        }
        return false;
    }

}

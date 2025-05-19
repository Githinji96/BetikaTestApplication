package com.commontests;

import com.PropertyData.loadProperty;
import com.reRunFailedTests.rerunFailedTestCases;
import com.utils.DriverClass;
import com.utils.ExtentReportManager;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.*;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

@Listeners(com.ListenersPackage.Listeners.class)
public class BrokenLinksTest {

    private WebDriver driver;
    private JavascriptExecutor js;
    private DriverClass driverClass;
    private String betUrl;

    @BeforeTest
    public void setupDriver() {
        try {
            System.out.println("Initializing WebDriver...");
            driverClass = new DriverClass("firefox");
            driver = driverClass.getDriver();

            if (driver == null) {
                throw new RuntimeException("WebDriver is not initialized after DriverClass setup");
            }

            js = (JavascriptExecutor) driver;
            PageFactory.initElements(driver, this);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize WebDriver");
        }
    }

    @BeforeTest(dependsOnMethods = "setupDriver")
    public void launchApplication() throws IOException {
        loadProperty propertyLoader = new loadProperty();
        propertyLoader.loadProperties();
        betUrl = propertyLoader.betUrl;
        driver.get(betUrl);
    }

    @Test(retryAnalyzer = rerunFailedTestCases.class)
    public void testBrokenLinks() {
        List<WebElement> links = driver.findElements(By.tagName("a"));

        for (WebElement link : links) {
            String url = link.getAttribute("href");
            System.out.println(url);

            if (url == null || url.isEmpty()) {
                System.out.println("URL is either not configured for anchor tag or it is empty");
                continue;
            }

            if (!url.startsWith(betUrl)) {
                System.out.println("URL belongs to another domain, skipping it.");
                continue;
            }

            try {
                HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                connection.setRequestMethod("HEAD");
                connection.connect();

                int responseCode = connection.getResponseCode();
                System.out.println(url + " responded with code: " + responseCode);
            } catch (MalformedURLException e) {
                System.err.println("Malformed URL: " + url);
                e.printStackTrace();
            } catch (IOException e) {
                System.err.println("IOException while connecting to URL: " + url);
                e.printStackTrace();
            }
        }
    }

    @AfterTest
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }

    @AfterSuite
    public void generateReport() {
        ExtentReportManager.getReportInstance().flush();
        ExtentReportManager.convertHtmlToPdf();
    }
}

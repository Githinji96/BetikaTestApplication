package com.commontests;

import com.PropertyData.loadProperty;
import com.reRunFailedTests.rerunFailedTestCases;
import com.utils.DriverClass;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;


public class Test_BrokenLinks {

    public String betUrl;
    String browser = "chrome";
    WebDriver driver;
    private DriverClass driverClass;
    private JavascriptExecutor js;

    public Test_BrokenLinks() {
        // initializing the pageObjects
        DriverClass driverClass = new DriverClass(browser);
        driver = driverClass.driver;
        PageFactory.initElements(driver, this);

    }
    @BeforeTest
    public void launchUrl() throws IOException {
        loadProperty ld= new loadProperty();
        ld.loadProperties();
        this.betUrl=ld.betUrl;
        driver.get(betUrl);
    }

    @AfterTest
    public void tearDown() {
        driver.quit();
        driver = null;
    }

    @Test(retryAnalyzer = rerunFailedTestCases.class)
    public void testBrokenLinks() {

            String url = "";
            HttpURLConnection huc = null;
            int respCode = 200;
            List<WebElement> links = driver.findElements(By.tagName("a"));

            Iterator<WebElement> it = links.iterator();

            while (it.hasNext()) {

                url = it.next().getAttribute("href");
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
                    huc = (HttpURLConnection) (new URL(url).openConnection());

                    huc.setRequestMethod("HEAD");

                    huc.connect();
                    if (respCode > 400) {
                        System.out.println(url + " is a broken link");
                    } else {
                        System.out.println(url + " is a valid link");
                    }

                } catch (MalformedURLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }


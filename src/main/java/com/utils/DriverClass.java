package com.utils;


import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;

public class DriverClass {

    public String browser;
    public WebDriver driver;
    public WebDriverWait wait;
    public JavascriptExecutor js;

    // browser variable holds the name of the browser
    public DriverClass(String browser) {
        this.browser = browser;
        setup();
    }

    // default browser-no-config
    public DriverClass() {
        this.browser = "chrome";
        setup();
    }

    public void setup() {
        if (browser.equalsIgnoreCase("firefox")) {
            WebDriverManager.firefoxdriver().setup();
            driver = new FirefoxDriver();
        } else if (browser.equalsIgnoreCase("chrome")) {
            WebDriverManager.chromedriver().setup();
            driver = new ChromeDriver();
        } else if (browser.equalsIgnoreCase("Edge")) {
            WebDriverManager.edgedriver().setup();
            driver = new EdgeDriver();
        }

        js = (JavascriptExecutor) driver;

        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.manage().deleteAllCookies();
        driver.manage().window().maximize();
    }
    public void customWait(WebDriver dr,Integer duration, WebElement element){
        new WebDriverWait(dr, Duration.ofSeconds(duration)).until(ExpectedConditions.visibilityOf(element));
    }
    public String takeScreenshot( String fileName) {
        try {
            String baseDir = System.getProperty("user.dir");
            String screenshotDirPath = baseDir + File.separator + "screenshots";
            File screenshotDir = new File(screenshotDirPath);

            if (!screenshotDir.exists()) {
                screenshotDir.mkdirs();
            }

            TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
            File sourceFile = takesScreenshot.getScreenshotAs(OutputType.FILE);

            // Sanitize filename
            fileName = fileName.replaceAll("[^a-zA-Z0-9.-]", "_");

            // Create destination file path
            String destPath = screenshotDirPath + File.separator + fileName;
            File destFile = new File(destPath);

            FileUtils.copyFile(sourceFile, destFile);

            System.out.println("Screenshot saved: " + destPath);
            return destPath; // Return full path
        } catch (Exception e) {
            System.out.println("Error while saving screenshot: " + e.getMessage());
            return null;
        }
    }

    public WebDriver getDriver() {
        if (driver == null) {
            throw new RuntimeException("WebDriver is not initialized");
        }
        return driver;
    }
}

package com.utils;


import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
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

       	ChromeOptions options = new ChromeOptions();
           options.addArguments("--remote--allow-origins=*");

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
    public void takeScreenshot(String fileName) throws IOException {
        TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
	File sourceFile = takesScreenshot.getScreenshotAs(OutputType.FILE);
	File destFile = new File("./screenshots/"+fileName);
	FileUtils.copyFile(sourceFile, destFile);
	System.out.println("Screenshot saved successfully");
    }
}

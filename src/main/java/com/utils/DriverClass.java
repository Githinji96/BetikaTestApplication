package com.utils;


import java.time.Duration;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeSuite;

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
}

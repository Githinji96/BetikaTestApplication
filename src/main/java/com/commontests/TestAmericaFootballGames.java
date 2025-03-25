package com.commontests;

import com.PropertyData.loadProperty;
import com.loginpackage.AppLogin;
import com.reRunFailedTests.rerunFailedTestCases;
import com.utils.DriverClass;
import com.utils.ExtentReportManager;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Listeners(com.ListenersPackage.Listeners.class)
public class TestAmericaFootballGames {
    WebDriver driver;
    JavascriptExecutor js;
    DriverClass driverClass;

    public String URL;
    public String usernumber;
    public String password;

    @FindBy(xpath="//span[@class='sports-list__item__label narrow' and text()='American Football']")
    WebElement americanftball;

    @FindBy(xpath="//button[normalize-space()='See all upcoming american football matches']")
    WebElement upcomingmatches;

    @FindBy(xpath="//a[@class='prebet-match__markets'][1]")
    WebElement clickmarkets;

    @FindBy(className = "stacked__details")
    WebElement printselectedOption;

    public TestAmericaFootballGames() {
        // initializing the pageObjects
        driverClass = new DriverClass("chrome");
        driver = driverClass.driver;
        js = driverClass.js;
        PageFactory.initElements(driver, this);
    }

    @BeforeMethod
    public void login() {
        loadProperty ld = new loadProperty();
        try {
            ld.loadProperties();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // Assign the loaded properties to the local instance variables
        this.URL = ld.URL;
        this.usernumber = ld.userNumber;
        this.password = ld.password;

        AppLogin lg = new AppLogin();
        lg.login(URL, usernumber, password, new ArrayList<>(Arrays.asList(driver, js)));
    }

    @Test(retryAnalyzer = rerunFailedTestCases.class)
    public void TestAmericanFootballGames() {
        try {
            js.executeScript("arguments[0].click()", americanftball);
            js.executeScript("arguments[0].click()", upcomingmatches);
            clickmarkets.click();
            // Select and place random market in one single game
            List<WebElement> options = driver.findElements(By.className("market__odds"));

            if (options.isEmpty()) {
                System.out.println("American Football games not found.");
                return;
            }

            int rSize = (int) Math.floor(Math.random() * options.size());
            List<WebElement> oddButtons = options.get(rSize).findElements(By.className("odd"));

            js.executeScript("arguments[0].click()", oddButtons.get((int) Math.floor(Math.random() * oddButtons.size())));
            // Get the randomly selected option bet
            System.out.println(printselectedOption.getText());
        } catch (NoSuchElementException e) {
            System.out.println("American Football games not found.");
        }
    }

    @AfterSuite
    public void getReport() {
        // Save the Extent Report
        ExtentReportManager.getReportInstance().flush();

        // Convert the HTML report to PDF
        ExtentReportManager.convertHtmlToPdf();
    }
}

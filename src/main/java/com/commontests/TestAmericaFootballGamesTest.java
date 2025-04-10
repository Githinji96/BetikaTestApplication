package com.commontests;

import com.PropertyData.loadProperty;
import com.loginpackage.AppLogin;
import com.reRunFailedTests.rerunFailedTestCases;
import com.utils.DriverClass;
import com.utils.ExtentReportManager;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Listeners(com.ListenersPackage.Listeners.class)
public class TestAmericaFootballGamesTest {
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

    @BeforeTest
    public void setup() {
        try {
            System.out.println("Initializing WebDriver...");
            driverClass = new DriverClass("chrome");
            driver = driverClass.getDriver();  // Ensure getDriver() method exists in DriverClass
            js = (JavascriptExecutor) driver;
            PageFactory.initElements(driver, this);

            if (driver == null) {
                throw new RuntimeException("WebDriver is not initialized after DriverClass setup");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize WebDriver");
        }
    }

    @BeforeTest(dependsOnMethods = "setup")
    private void login() throws IOException {
        loadProperty ld= new loadProperty();
        ld.loadProperties();
        // Assign the loaded properties to the local instance variables
        this.URL = ld.URL;
        this.usernumber = ld.userNumber;
        this.password = ld.password;

        //Lauch login
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

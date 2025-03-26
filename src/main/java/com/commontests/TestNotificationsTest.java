package com.commontests;

import com.PropertyData.loadProperty;
import com.reRunFailedTests.rerunFailedTestCases;
import com.loginpackage.AppLogin;
import com.utils.DriverClass;
import com.utils.ExtentReportManager;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.*;

import java.io.IOException;
import java.util.*;
import java.util.ArrayList;
import java.util.Arrays;

@Listeners(com.ListenersPackage.Listeners.class)
public class TestNotificationsTest {

    WebDriver driver;
    JavascriptExecutor js;
    DriverClass driverClass;

    public String URL;
    public String userNumber;
    public String password;

    @FindBy(xpath="//button[@class='user-notifications__toggle']//span[@class='visible-desktop'][normalize-space()='Notifications']")
    WebElement notification;
    // Class constructor
    public TestNotificationsTest() {
        // initializing the pageObjects
        driverClass = new DriverClass("firefox");
        driver = driverClass.driver;
        js = driverClass.js;
        PageFactory.initElements(driver, this);
    }

    @BeforeMethod
    public void login() throws IOException {
        loadProperty ld= new loadProperty();
        ld.loadProperties();
        // Assign the loaded properties to the local instance variables
        this.URL = ld.URL;
        this.userNumber = ld.userNumber;
        this.password = ld.password;

        AppLogin lg = new AppLogin();
        lg.login(URL, userNumber, password, new ArrayList<>(Arrays.asList(driver, js)));


    }
    @Test(retryAnalyzer = rerunFailedTestCases.class)
    public void notifications_Test(){
        notification.click();
        List<WebElement> listItems = driver.findElements(By.className("user-notifications__list"));

        // Iterate through the list and print the text of each element
        if (listItems.isEmpty()) {
            System.out.println("No notifications Found");
        } else {
            // Iterate through the list and print the text of each element
            for (WebElement listItem : listItems) {

                System.out.println(listItem.getText());
            }
        }
    }

    @AfterTest
    public void tearDown() {
        driver.quit();
        driver = null;
    }
    @AfterSuite
    public void getReport() {
        // Save the Extent Report
        ExtentReportManager.getReportInstance().flush();

        // Convert the HTML report to PDF
        ExtentReportManager.convertHtmlToPdf();
    }
}

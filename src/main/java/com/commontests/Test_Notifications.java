package com.commontests;

import com.PropertyData.loadProperty;
import com.reRunFailedTests.rerunFailedTestCases;
import com.loginpackage.AppLogin;
import com.utils.DriverClass;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Test_Notifications {

    WebDriver driver;
    JavascriptExecutor js;
    DriverClass driverClass;

    public String URL;
    public String usernumber;
    public String password;

    @FindBy(xpath="//button[@class='user-notifications__toggle']//span[@class='visible-desktop'][normalize-space()='Notifications']")
    WebElement notification;
    // Class constructor
    public Test_Notifications() {
        // initializing the pageObjects
        driverClass = new DriverClass("chrome");
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
        this.usernumber = ld.usernumber;
        this.password = ld.password;

        AppLogin lg = new AppLogin();
        lg.login(URL, usernumber, password, new ArrayList<>(Arrays.asList(driver, js)));


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
}

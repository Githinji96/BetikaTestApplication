package com.commontests;

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
import java.util.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Test_Notifications {

    WebDriver driver;
    JavascriptExecutor js;
    DriverClass driverClass;
    public String URL = "https://www.betika.com/en-ke/login";

    @FindBy(xpath="//button[@class='user-notifications__toggle']//span[@class='visible-desktop'][normalize-space()='Notifications']")
    WebElement notification;
    // Class constructor
    public Test_Notifications() {
        // initializing the pageObjects
        driverClass = new DriverClass();
        driver = driverClass.driver;
        js = driverClass.js;
        PageFactory.initElements(driver, this);
    }

    @BeforeMethod
    private void login() {
        AppLogin lg = new AppLogin();
        lg.login(URL, "0711198013", "Bg33173375", new ArrayList<>(Arrays.asList(driver, js)));
    }
    @Test
    public void notifications_Test(){
        notification.click();
        List<WebElement> listItems = driver.findElements(By.className("user-notifications__list"));

        // Iterate through the list and print the text of each element
        for (WebElement listItem : listItems) {
            System.out.println(listItem.getText());
        }
    }
    @AfterTest
    public void tearDown() {
        driver.quit();
        driver = null;
    }
}

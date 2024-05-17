package com.loginpackage;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.ArrayList;


public class AppLogin {

    // page factory
    //phone number login locator
    @FindBy(xpath = "//input[@placeholder='e.g. 0712 234567']")
    WebElement phoneNumber;

    // passwordlocator
    @FindBy(xpath = "//input[@type='password']")
    WebElement password;

    //login button
    @FindBy(xpath = "//button[@class='button account__payments__submit session__form__button login button button__secondary']")
    WebElement loginBtn;

    public void login(String url, String contactNo, String pass, ArrayList<?> drDetails) {
        try {
            PageFactory.initElements(((WebDriver) (drDetails.get(searchObject(drDetails, "driver")))), this);

            int index = searchObject(drDetails, "driver");

            ((WebDriver) (drDetails.get(index))).get(url);
            phoneNumber.sendKeys(contactNo);
            password.sendKeys(pass);
            ((JavascriptExecutor) (drDetails.get(searchObject(drDetails, "jsExecutor")))).executeScript("arguments[0].click();", loginBtn);
        } catch (UnhandledAlertException e) {
            System.err.println("Unhandled alert Error!");
        }
    }

    private int searchObject(ArrayList<?> props, String searchTerm) {
        for (Object member : props) {
            if ((member instanceof WebDriver) && (searchTerm.equalsIgnoreCase("driver"))) {
                return props.indexOf(member);
            } else if ((member instanceof JavascriptExecutor) && (searchTerm.equalsIgnoreCase("jsExecutor"))) {
                return props.indexOf(member);
            }
        }
        return 0;
    }
}

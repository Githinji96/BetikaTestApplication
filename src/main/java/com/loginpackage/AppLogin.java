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

    //login method that is passed to every test class
    public void login(String url, String contactNo, String pass, ArrayList<?> drDetails) {
        try {
            int driverIndex = searchObject(drDetails, "driver");
            int jsIndex = searchObject(drDetails, "jsExecutor");

            if (driverIndex == -1 || jsIndex == -1) {
                throw new RuntimeException("Required WebDriver or JavascriptExecutor object not found in the list.");
            }

            WebDriver driver = (WebDriver) drDetails.get(driverIndex);
            JavascriptExecutor jsExecutor = (JavascriptExecutor) drDetails.get(jsIndex);

            PageFactory.initElements(driver, this);
            driver.get(url);
            phoneNumber.sendKeys(contactNo);
            password.sendKeys(pass);
            jsExecutor.executeScript("arguments[0].click();", loginBtn);

        } catch (UnhandledAlertException e) {
            System.err.println("Unhandled alert Error!");
        } catch (Exception e) {
            System.err.println("An error occurred during login: " + e.getMessage());
        }
    }

    private int searchObject(ArrayList<?> props, String searchTerm) {
        for (int i = 0; i < props.size(); i++) {
            Object member = props.get(i);
            if (member instanceof WebDriver && searchTerm.equalsIgnoreCase("driver")) {
                return i;
            } else if (member instanceof JavascriptExecutor && searchTerm.equalsIgnoreCase("jsExecutor")) {
                return i;
            }
        }
        // Return -1 if not found to better indicate an error
        return -1;
    }

}

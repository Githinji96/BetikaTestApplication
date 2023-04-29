package com.baseClassPackage;

import java.util.List;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class RegisterNewUser {
    WebDriver driver;
    JavascriptExecutor js;
    public String URL = "https://www.betika.com/en-ke";

    //register link
    @FindBy(linkText = "Register")
    WebElement register;

    //phone number textfield
    @FindBy(xpath = "//input[@pattern='^([1-9]\\d{0,2}(,\\d{3}){0,3})$|^([1-9]\\d?(,\\d{3}){4})$']")
    WebElement phonenumber;

    //passwords textfield
    @FindBy(xpath = "//input[@type='password']")
    List<WebElement> passwordFields;

    //Check accept radio button
    @FindBy(xpath = "//span[@class='checkmark']")
    WebElement acceptTerms;

    //submit button
    @FindBy(xpath = "//button[contains(@class, 'button') and contains(@class, 'account__payments__submit') and contains(@class, 'session__form__button') and contains(@class, 'login') and contains(@class, 'button') and contains(@class, 'button__secondary')]")
    WebElement submitBtn;

    public RegisterNewUser() {
        String browser = "firefox";
        DriverClass driverClass = new DriverClass(browser);
        driver = driverClass.driver;
        js = driverClass.js;
        PageFactory.initElements(driver, this);

    }

    @BeforeTest
    public void visitUrl() {

        driver.get(URL);
    }

    @AfterTest
    public void tearDown() {
        driver.quit();
        driver = null;
    }

    @Test
    public void registeruser() {
        register.click();
        phonenumber.sendKeys("07250000");
        passwordFields.forEach(password -> {
            password.sendKeys("password1234test@" + Keys.ENTER);
        });
        js.executeScript("arguments[0].click()", acceptTerms);
        js.executeScript("arguments[0].click()", submitBtn);

    }

}


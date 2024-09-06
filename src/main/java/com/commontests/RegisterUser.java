package com.commontests;

import com.PropertyData.loadProperty;
import com.randompackage.GenerateRandomData;
import com.reRunFailedTests.rerunFailedTestCases;
import com.utils.DriverClass;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;


public class RegisterUser {
    WebDriver driver;
    JavascriptExecutor js;
    private String URL = "https://www.betika.com/en-ke";

    //register link
    @FindBy(linkText = "Register")
    WebElement register;

    //phone number textfield
    @FindBy(xpath = "//input[@placeholder='e.g. 0712 234567']")
    WebElement phoneNumber;

    //passwords textfield
    @FindAll(@FindBy(xpath = "//input[@type='password']"))
    List<WebElement> passwordFields;

    //Check accept radio button
    @FindBy(className = "checkmark")
    WebElement acceptTerms;

    //submit button
    @FindBy(xpath = "//button[contains(@class, 'button') and contains(@class, 'account__payments__submit') and contains(@class, 'session__form__button') and contains(@class, 'login') and contains(@class, 'button') and contains(@class, 'button__secondary')]")
    WebElement submitBtn;

    private final DriverClass driverClass;
    private String betUrl;

    public RegisterUser() {

        driverClass = new DriverClass("edge");
        driver = driverClass.driver;
        js = driverClass.js;
        PageFactory.initElements(driver, this);

    }

    @BeforeTest
    public void visitUrl() throws IOException {
        loadProperty ld= new loadProperty();
        ld.loadProperties();
        this.betUrl=ld.betUrl;
        driver.get(betUrl);

    }

    @AfterTest
    public void tearDown() {
      driver.quit();
        driver = null;
    }

    @Test
    public void registerUser() {
        register.click();
        String password = GenerateRandomData.randomName(8);
        phoneNumber.clear();
        phoneNumber.sendKeys(GenerateRandomData.randomNum());

        for (WebElement field : passwordFields) {
            field.sendKeys(password);
        }
        js.executeScript("arguments[0].click()", acceptTerms);
        js.executeScript("arguments[0].click()", submitBtn);

    }
}





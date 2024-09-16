package com.commontests;


import com.PropertyData.loadProperty;
import com.reRunFailedTests.rerunFailedTestCases;
import com.loginpackage.AppLogin;

import com.utils.DriverClass;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SelectMultipleSoccerbets {
    WebDriver driver;
    JavascriptExecutor js;

    public String URL;
    public String usernumber;
    public String password;

    //placebet button
    @FindBy(xpath = "//button[contains(@class, 'account__payments__submit') and contains(@class, 'betslip__details__button__place')]")
    WebElement submit;

    @FindAll(@FindBy(className ="rounded-card" ))
    public List<WebElement>roundedCard;

    @FindAll(@FindBy(className = "prebet-match__odds"))
    List<WebElement> teams;

    @FindBy(className = "modal__container")
    WebElement modalContainer;

    public SelectMultipleSoccerbets() {

        DriverClass driverClass = new DriverClass("edge");
        driver = driverClass.driver;
        js = driverClass.js;
        PageFactory.initElements(driver, this);

    }

    @Test(retryAnalyzer = rerunFailedTestCases.class)
    public void placeBets() throws UnhandledAlertException, IOException {
        loadProperty ld= new loadProperty();
        ld.loadProperties();

        // Assign the loaded properties to the local instance variables
        this.URL = ld.URL;
        this.usernumber = ld.usernumber;
        this.password = ld.password;
        AppLogin lg = new AppLogin();
        lg.login(URL, usernumber, password, new ArrayList<>(Arrays.asList(driver, js)));

        // Home, draw or away buttons
        List<WebElement> odds;
        // Randomly choose team and place 4 bets
        for (int i = 0; i <10; i++) {
            int randomTeam = (int) Math.floor(Math.random() * teams.size());
            odds = teams.get(randomTeam).findElements(By.className("prebet-match__odd"));
            int randId = (int) Math.floor(Math.random() * odds.size());
            js.executeScript("arguments[0].click()", odds.get(randId));

            //print either homewin, draw or awaywin for the matches choosen
            System.out.println(randId);
        }
        System.out.println("Number of matches. " + teams.size());
         // get all matches selected in betslip
        for (WebElement listItem : roundedCard) {
            System.out.println(listItem.getText());
        }
        // Submit the bets
        js.executeScript("arguments[0].click()", submit);

        //display modal container when insufficient amount is placed.
        Assert.assertTrue(modalContainer.isDisplayed(), "The modal container is not displayed on the page.");

    }

    @AfterTest
    public void tearDown() {
       // driver.quit();
        driver = null;

    }
}

package com.commontests;


import com.loginpackage.AppLogin;

import com.utils.DriverClass;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SelectMultiplebets {
    WebDriver driver;
    JavascriptExecutor js;

    public String URL = "https://www.betika.com/en-ke/login";

    //placebet button
    @FindBy(xpath = "//button[contains(@class, 'account__payments__submit') and contains(@class, 'betslip__details__button__place')]")
    WebElement submit;

    @FindAll(@FindBy(className = "prebet-match__odds"))
    List<WebElement> teams;

    public SelectMultiplebets() {
        String browser = "chrome";
        DriverClass driverClass = new DriverClass(browser);
        driver = driverClass.driver;
        js = driverClass.js;
        PageFactory.initElements(driver, this);

    }

    @Test
    public void placeBets() throws UnhandledAlertException {
        AppLogin lg = new AppLogin();

        lg.login(URL, "0711198013", "Bg33173375", new ArrayList<>(Arrays.asList(driver, js)));

        // Home, draw or away buttons
        List<WebElement> odds;
        // Randomly choose team and place 4 bets
        for (int i = 0; i < 20; i++) {
            int randomTeam = (int) Math.floor(Math.random() * teams.size());
            odds = teams.get(randomTeam).findElements(By.className("prebet-match__odd"));
            int randId = (int) Math.floor(Math.random() * odds.size());
            js.executeScript("arguments[0].click()", odds.get(randId));
            //print either homewin, draw or awaywin for the matches choosen
            System.out.println(randId);
        }
        System.out.println("Number of matches. " + teams.size());
        // Submit the bets
        js.executeScript("arguments[0].click()", submit);
    }

    @AfterTest
    public void tearDown() {
        // driver.quit();
        driver = null;

    }
}

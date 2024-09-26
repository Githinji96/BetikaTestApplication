package com.commontests;


import com.PropertyData.loadProperty;
import com.reRunFailedTests.rerunFailedTestCases;
import com.loginpackage.AppLogin;

import com.utils.DriverClass;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;

import java.io.IOException;
import java.time.Duration;
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

    @FindBy(xpath = "//input[@placeholder='Enter stake']")
    WebElement enterAmt;

    @FindBy(className="stacked__details")
    WebElement betslip;

    @FindBy(className = "stacked__odd")
    WebElement oddValue;

    @FindBy(className = "betslip__details__row__value")
    WebElement accountBalance;


    @FindBy(className = "modal__container")
    WebElement modalContainer;

    @FindBy(className = "modal__x")
    WebElement cancelModal;

    @FindBy(className= "nav__item__label")
    WebElement betslipInfo;


    public SelectMultipleSoccerbets() {

        DriverClass driverClass = new DriverClass("Chrome");
        driver = driverClass.driver;
        js = driverClass.js;
        PageFactory.initElements(driver, this);

    }

   // @Test(retryAnalyzer = rerunFailedTestCases.class)
    @Test
    public void login() throws UnhandledAlertException, IOException {
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
        // enter the amount to place a bet
        enterAmt.sendKeys(Keys.CONTROL + "a" + Keys.BACK_SPACE);
        enterAmt.sendKeys("99");

        //get bet balance amount
        String amnt=accountBalance.getText();
        String amnt1 = amnt.replaceAll("KES", "");

        // Parse the remaining string to a double value
        double accBalance = Double.parseDouble(amnt1);
        System.out.println("==============================");
        System.out.println("The account balance is ksh "+accBalance);

        if(accBalance<99){
            //click submit button to verify modal container is displayed when insufficient amount is placed.
            js.executeScript("arguments[0].click()", submit);
            //display modal container when insufficient amount is placed.
            Assert.assertTrue(modalContainer.isDisplayed(), "The modal container is not displayed on the page.");
            //cancel the modal
            cancelModal.click();

        }else{
            // click submit button to place a bet
            js.executeScript("arguments[0].click()", submit);

            //notification-show success > title of the bet placed
            WebElement toast = driver.findElement (
                    By.xpath("//div[contains(@class, 'notification') and contains(@class, 'show') and contains(@class, 'success')]//div[@class='title']"));
            new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.visibilityOf(toast));
            Assert.assertTrue(toast.getText().contains("Bet Placement Successful"));
        }
        //click Mybets button
        betslipInfo.click();

        //print successfull bets ID placed
        List<WebElement> listItems = driver.findElements(By.className("bets"));
        if(listItems.isEmpty()){
          System.out.println(" No successfull bets placed");
        }else{

        for (WebElement listItem : listItems) {
            System.out.println("==============================");
            System.out.println(listItem.getText());
        }
        }
    }

    @AfterTest
    public void tearDown() {
       // driver.quit();
        //driver = null;

    }
}

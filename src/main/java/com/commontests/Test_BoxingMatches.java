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
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Test_BoxingMatches {

    WebDriver driver;
    JavascriptExecutor js;
    DriverClass driverClass;

    public String URL;
    public String usernumber;
    public String password;

    @FindBy(xpath="//span[normalize-space()='Boxing']")
    WebElement boxingbtn;

    @FindAll(@FindBy(className = "prebet-match__odds"))
    List<WebElement> teams;

    @FindBy(xpath="//button[normalize-space()='See all upcoming boxing matches']")
    WebElement upcomingmatches;

    @FindBy(className = "betslip__details__row__value")
    WebElement accountBalance;

    @FindBy(xpath="//div[@class='rounded-card']")
    WebElement betslip;

    @FindBy(className = "stacked__odd")
    WebElement oddValue;

    @FindBy(xpath = "//button[contains(@class, 'account__payments__submit') and contains(@class, 'betslip__details__button__place')]")
    WebElement submit;

    @FindBy(xpath = "//input[@placeholder='Enter stake']")
    WebElement enterAmt;

    // Class constructor
    public Test_BoxingMatches() {
        // initializing the pageObjects
        driverClass = new DriverClass();
        driver = driverClass.driver;
        js = driverClass.js;
        PageFactory.initElements(driver, this);
    }

    @BeforeMethod
    private void login() throws IOException {
        loadProperty ld= new loadProperty();
        ld.loadProperties();
        // Assign the loaded properties to the local instance variables
        this.URL = ld.URL;
        this.usernumber = ld.usernumber;
        this.password = ld.password;

        //Lauch login
        AppLogin lg = new AppLogin();
        lg.login(URL, usernumber, password, new ArrayList<>(Arrays.asList(driver, js)));
    }

    @Test(testName="PlacetestBoxingMatches", retryAnalyzer = rerunFailedTestCases.class)
    public void PlacetestBoxingMatches(){
        boxingbtn.click();
        // Handle case where the upcoming matches button might not be present
        try {
           upcomingmatches.click();
        } catch (NoSuchElementException e) {
            System.out.println("Upcoming matches button not found, proceeding with available matches.");
        }


        List<WebElement> odds;
        // Randomly choose team and place 4 bets
        for (int i = 0; i <6; i++) {
            int randomTeam = (int) Math.floor(Math.random() * teams.size());
            odds = teams.get(randomTeam).findElements(By.className("prebet-match__odd"));
            int randId = (int) Math.floor(Math.random() * odds.size());
            js.executeScript("arguments[0].click()", odds.get(randId));
            //print either homewin, draw or awaywin for the matches choosen
            System.out.println(randId);
        }

            enterAmt.sendKeys(Keys.CONTROL + "a" + Keys.BACK_SPACE);
            enterAmt.sendKeys("88");

            //Verify the betslip informations
            System.out.println("The betslip "+betslip.getText());
            System.out.println("The total odds "+oddValue.getText());

            //get bet balance amount
            String amnt=accountBalance.getText();
            String amnt1 = amnt.replaceAll("KES", "");

            // Parse the remaining string to a double value
            double accBalance = Double.parseDouble(amnt1);
            System.out.println("The account balance "+accBalance);

            //place bet if the account balance amount is greater than 88.
            if(accBalance>=88){
                js.executeScript("arguments[0].click()", submit);
                //notification-show success > title
                WebElement toast = driver.findElement(
                        By.xpath("//div[contains(@class, 'notification') and contains(@class, 'show') and contains(@class, 'success')]//div[@class='title']"));
                new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.visibilityOf(toast));
                Assert.assertTrue(toast.getText().contains("Bet Placement Successful"));
            }
            else{
                System.out.println("Amount is less in the account to place a bet");
            }
        }

    }



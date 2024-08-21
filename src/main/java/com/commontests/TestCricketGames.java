package com.commontests;

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

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestCricketGames {
    WebDriver driver;
    JavascriptExecutor js;
    DriverClass driverClass;
    public String URL = "https://www.betika.com/en-ke/login";

    @FindBy(xpath= "//span[@class='sports-list__item__label narrow'][normalize-space()='Cricket']")
    WebElement cricketbtn;


    @FindAll(@FindBy(className = "prebet-match__odds"))
    List<WebElement> teams;

    //Amount placeholder
    @FindBy(xpath = "//input[@placeholder='Enter stake']")
    WebElement enterAmt;

    @FindBy(className = "stacked__odd")
    WebElement oddValue;
    @FindBy(className = "betslip__details__row__value")
    WebElement accountBalance;

    @FindBy(xpath="//div[@class='rounded-card']")
    WebElement betslip;
    //placebet button
    @FindBy(xpath = "//button[contains(@class, 'account__payments__submit') and contains(@class, 'betslip__details__button__place')]")
    WebElement submit;



    public TestCricketGames(){
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
    public void selectMultipleCricketGames(){
         cricketbtn.click();
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
        System.out.println("Number of matches. " + teams.size());

        enterAmt.sendKeys(Keys.CONTROL + "a" + Keys.BACK_SPACE);
        enterAmt.sendKeys("99");

        //Verify the betslip informations
        System.out.println("The betslip "+betslip.getText());
        System.out.println("The total odds "+oddValue.getText());

        //get bet balance amount
        String amnt=accountBalance.getText();
        String amnt1 = amnt.replaceAll("KES", "");

        // Parse the remaining string to a double value
        double accBalance = Double.parseDouble(amnt1);
        System.out.println("The account balance "+accBalance);

        //place bet if the account balance amount is greater than 99.
        if(accBalance>=99){
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
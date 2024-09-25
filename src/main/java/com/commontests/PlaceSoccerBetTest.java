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
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import java.io.IOException;
import java.time.Duration;
import java.util.*;

public class PlaceSoccerBetTest {

    WebDriver driver;
    JavascriptExecutor js;
    DriverClass driverClass;

    public String URL;
    public String usernumber;
    public String password;

    //upcomingbtn
    @FindBy(xpath = "//button[contains(@class, 'match-filter__button') and contains(@class, 'main')]")
    WebElement filterbtn;

    @FindBy(xpath = "//button[normalize-space()='Tomorrow' or normalize-space()='Monday']")
    WebElement  gameDay;
    //highlight matches btn
    @FindBy(xpath = "(//button[normalize-space()='Apply'])[1]")
    WebElement apply;
    // click apply

    @FindBy(xpath = "(//a[@class='prebet-match__markets'][contains(normalize-space(), 'Markets')])[1]")
    WebElement clickbtn;

    @FindBy(xpath = "//a[@href='/en-ke/profile']")
    WebElement betProfile;

    //Amount placeholder
    @FindBy(xpath = "//input[@placeholder='Enter stake']")
    WebElement enterAmt;
    //placebet button
    @FindBy(xpath = "//button[contains(@class, 'account__payments__submit') and contains(@class, 'betslip__details__button__place')]")
    WebElement submit;

    @FindBy(className="stacked__details")
    WebElement betslip;

    @FindBy(className = "stacked__odd")
    WebElement oddValue;
    @FindBy(className = "betslip__details__row__value")
    WebElement accountBalance;

    @FindBy(className = "topnav__session__links__item")
    WebElement betProfileLink;

    @FindBy(className = "my-bets__button")
    WebElement openMadal;

    @FindBy(xpath = "//button[normalize-space()='All']")
    WebElement chooseAll;

    @FindAll(@FindBy(className = "bets"))
    public List<WebElement> placedBets;


    // Class constructor
    public PlaceSoccerBetTest() {
        // initializing the pageObjects
        driverClass = new DriverClass("chrome");
        driver = driverClass.driver;
        js = driverClass.js;
        PageFactory.initElements(driver, this);
    }

    @BeforeTest
    private void login() {
        loadProperty ld= new loadProperty();
        try {
            ld.loadProperties();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // Assign the loaded properties to the local instance variables
        this.URL = ld.URL;
        this.usernumber = ld.usernumber;
        this.password = ld.password;

        AppLogin lg = new AppLogin();
        lg.login(URL, usernumber, password, new ArrayList<>(Arrays.asList(driver, js)));
    }

    @Test(retryAnalyzer = rerunFailedTestCases.class)
    public void placebet() {

        String title = driver.getTitle();
        System.out.println(title);

        //Assert user successful login by viewing my bet page
        if (betProfile.isDisplayed() && betProfile.isEnabled()) {
            System.out.println("Login successfully");
        } else {
            System.out.println("Element is not displayed");
        }

        js.executeScript("arguments[0].click()",filterbtn);

        driverClass.customWait(driver, 5, apply);
        gameDay.click();
        apply.click();

        driverClass.customWait(driver,5,clickbtn);
        clickbtn.click();


        // Get all options within the match section (
        //Random select any market option
        List<WebElement> options = driver.findElements(By.className("market__odds"));

        int rSize = (int) Math.floor(Math.random()*options.size());

        List<WebElement> oddButtons = options.get(rSize)
                .findElements(By.className("odd"));

        js.executeScript("arguments[0].click()",
                oddButtons.get((int) Math.floor(Math.random()*oddButtons.size()))
        );
        System.out.println(rSize);

        driverClass.customWait(driver, 10, enterAmt);

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

         //place bet if the account balance amount is greater than kes99.
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
      //verify betslip
    @Test
     public void verifyMyBets(){
        betProfileLink.click();
        openMadal.click();
        chooseAll.click();
        // Iterate through the list and print all bets in account
        System.out.println("==================================================");
        for (WebElement listItem : placedBets) {
            System.out.println(listItem.getText());
        }
     }
     @AfterTest
     public void closeBrowser(){
        driver.close();
     }
}

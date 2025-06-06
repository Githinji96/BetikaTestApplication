package com.commontests;


import com.PropertyData.loadProperty;
import com.reRunFailedTests.rerunFailedTestCases;
import com.loginpackage.AppLogin;

import com.utils.DriverClass;

import com.utils.ExtentReportManager;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Listeners(com.ListenersPackage.Listeners.class)
public class SelectMultipleSoccerbetsTest {
    WebDriver driver;
    JavascriptExecutor js;

    public String URL;
    public String usernumber;
    public String password;
    private DriverClass driverClass;

    //placebet button
    @FindBy(xpath = "//button[contains(@class, 'account__payments__submit') and contains(@class, 'betslip__details__button__place')]")
    WebElement submit;

    @FindAll(@FindBy(className = "rounded-card"))
    public List<WebElement> roundedCard;

    @FindAll(@FindBy(className = "prebet-match__odds"))
    List<WebElement> teams;

    @FindAll(@FindBy(className = "prebet-match__odd-market__container"))
    public List<WebElement> oddsContainer;

    @FindBy(xpath = "//input[@placeholder='Enter stake']")
    WebElement enterAmt;


    @FindBy(className = "betslip__details__row__value")
    WebElement accountBalance;


    @FindBy(className = "modal__container")
    WebElement modalContainer;

    @FindBy(className = "modal__x")
    WebElement cancelModal;


    @BeforeTest
    public void setup() {
        try {
            System.out.println("Initializing WebDriver...");
            driverClass = new DriverClass("firefox");
            driver = driverClass.getDriver();  // Ensure getDriver() method exists in DriverClass
            js = (JavascriptExecutor) driver;
            PageFactory.initElements(driver, this);

            if (driver == null) {
                throw new RuntimeException("WebDriver is not initialized after DriverClass setup");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize WebDriver");
        }
    }

  @BeforeTest(dependsOnMethods = "setup")
    public void login() throws UnhandledAlertException, IOException {
        loadProperty ld = new loadProperty();
        ld.loadProperties();

        // Assign the loaded properties to the local instance variables
        this.URL = ld.URL;
        this.usernumber = ld.userNumber;
        this.password = ld.password;

        AppLogin lg = new AppLogin();
        lg.login(URL, usernumber, password, new ArrayList<>(Arrays.asList(driver, js)));

    }
   @Test(retryAnalyzer = rerunFailedTestCases.class)
    public void TestSelectionOfMultipleSoccerGames() {
       List<WebElement> listItems = oddsContainer;

           // Iterate through the list and print the text of each element
           for (WebElement listItem : listItems) {
               System.out.println("============================================");
               String gamesList=listItem.getText();
               if(gamesList.isEmpty()){
                   System.out.println("No games found");
               }
               else{
                   System.out.println(gamesList);
               }

           }

//  Iterate over all matches and randomly select an odd for each
       for (WebElement team : teams) {
           List<WebElement> odds = team.findElements(By.className("prebet-match__odd"));

           if (!odds.isEmpty()) { // Ensure there are odds available
               int randId = (int) Math.floor(Math.random() * odds.size());
               js.executeScript("arguments[0].click()", odds.get(randId));

               // Print selected odd (Home Win, Draw, or Away Win)
               System.out.println("Selected odd index: " + randId);
           }

       }

       System.out.println("Total matches selected: " + teams.size());


       // get all matches selected in betslip
        for (WebElement listItem : roundedCard) {
            System.out.println(listItem.getText());
        }
        // enter the amount to place a bet
        enterAmt.sendKeys(Keys.CONTROL + "a" + Keys.BACK_SPACE);
        enterAmt.sendKeys("99");

        //get bet balance amount
        String amnt = accountBalance.getText();
        String amnt1 = amnt.replaceAll("KES", "");

        // Parse the remaining string to a double value
        double accBalance = Double.parseDouble(amnt1);
        System.out.println("==============================");
        System.out.println("The account balance is ksh " + accBalance);

        if (accBalance <99) {
            //click submit button to verify modal container is displayed when insufficient amount is placed.
            js.executeScript("arguments[0].click()", submit);
            //display modal container when insufficient amount is placed.
            Assert.assertTrue(modalContainer.isDisplayed(), "The modal container is not displayed on the page.");
            //cancel the modal
            cancelModal.click();

        } else {
            // click submit button to place a bet
            js.executeScript("arguments[0].click()", submit);

            //notification-show success > title of the bet placed
            WebElement toast = driver.findElement(
                    By.xpath("//div[contains(@class, 'notification') and contains(@class, 'show') and contains(@class, 'success')]//div[@class='title']"));
            new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.visibilityOf(toast));
            Assert.assertTrue(toast.getText().contains("Bet Placement Successful"));

        }
    }

    @AfterTest
    public void tearDown() {
      // driver.quit();

    }
    @AfterSuite
    public void getReport() {
        // Save the Extent Report
        ExtentReportManager.getReportInstance().flush();

        // Convert the HTML report to PDF
        ExtentReportManager.convertHtmlToPdf();
    }
}

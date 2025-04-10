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
public class CricketGamesTest {
    WebDriver driver;
    JavascriptExecutor js;
    DriverClass driverClass;

    public String URL;
    public String usernumber;
    public String password;

    @FindBy(xpath= "//span[@class='sports-list__item__label narrow'][normalize-space()='Cricket']")
    WebElement cricketbtn;

    @FindAll(@FindBy(className = "prebet-match__odd-market__container"))
    public List<WebElement> oddsContainer;

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



    @BeforeTest
    public void setup() {
        try {
            System.out.println("Initializing WebDriver...");
            driverClass = new DriverClass("chrome");
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
    private void login() throws IOException {
        loadProperty ld= new loadProperty();
        ld.loadProperties();
        // Assign the loaded properties to the local instance variables
        this.URL = ld.URL;
        this.usernumber = ld.userNumber;
        this.password = ld.password;

        //Lauch login
        AppLogin lg = new AppLogin();
        lg.login(URL, usernumber, password, new ArrayList<>(Arrays.asList(driver, js)));
    }
    @Test(retryAnalyzer = rerunFailedTestCases.class)
    public void selectMultipleCricketGames(){
         cricketbtn.click();

// Iterate over all matches and randomly select an odd for each
        for (WebElement team : teams) {
            List<WebElement> odds = team.findElements(By.className("prebet-match__odd"));

            // Ensure there are odds available
            if (!odds.isEmpty()) {
                int randId = (int) Math.floor(Math.random() * odds.size());
                js.executeScript("arguments[0].click()", odds.get(randId));

                // Print selected odd (Home Win, Draw, or Away Win)
                System.out.println("Selected odd index: " + randId);
            }
        }

        System.out.println("Total matches selected: " + teams.size());


        enterAmt.sendKeys(Keys.CONTROL + "a" + Keys.BACK_SPACE);
        enterAmt.sendKeys("99");

        //Verify the betslip informations
        System.out.println("The betslip "+betslip.getText());

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
    @Test
    public void printTeamsAndMarketOdds() throws InterruptedException {
        js.executeScript("arguments[0].click()",cricketbtn);
        Thread.sleep(3000);
        List<WebElement> listItems = oddsContainer;

        // Iterate throu    h the list and print the text of each element
        if (listItems.isEmpty()) {
            System.out.println("No  matches Games Found");
        } else {
            // Iterate through the list and print the text of each element
            for (WebElement listItem : listItems) {
                System.out.println("============================================");
                System.out.println(listItem.getText());
            }
        }
    }
    @AfterSuite
    public void getReport() {
        // Save the Extent Report
        ExtentReportManager.getReportInstance().flush();

        // Convert the HTML report to PDF
        ExtentReportManager.convertHtmlToPdf();
    }
}

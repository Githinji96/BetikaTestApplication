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
public class BoxingMatchesTest {

    WebDriver driver;
    JavascriptExecutor js;
   public DriverClass driverClass;

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

        //Launch login
        AppLogin lg = new AppLogin();
        lg.login(URL, usernumber, password, new ArrayList<>(Arrays.asList(driver, js)));
    }

    @Test(testName="PlacetestBoxingMatches", retryAnalyzer = rerunFailedTestCases.class)
    public void placetestBoxingMatches(){
        boxingbtn.click();
        // Handle case where the upcoming matches button might not be present
        try {
            js.executeScript("arguments[0].click()",upcomingmatches);
        } catch (NoSuchElementException e) {

        }
        List<WebElement> odds;

        // Loop to place bets on multiple matches
        for (int i = 0; i < 6; i++) {
            // Randomly select a team index
            int randomTeamIndex = (int) Math.floor(Math.random() * teams.size());

            // Retrieve all betting odds for the selected team
            odds = teams.get(randomTeamIndex).findElements(By.className("prebet-match__odd"));

            // Check if there are any odds available for the selected team
            if (odds.isEmpty()) {
                System.out.println("No betting odds available for the selected team.");
                continue; // Skip this iteration if no odds are found
            }

            // Randomly select an odd from the retrieved list
            int randomOddIndex = (int) Math.floor(Math.random() * odds.size());

            // Place the bet by clicking the selected odd
            js.executeScript("arguments[0].click()", odds.get(randomOddIndex));

            // Print the selected odd index and the type of bet placed
            System.out.println("Bet placed on odd index: " + randomOddIndex + " for team index: " + randomTeamIndex);
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
    @AfterSuite
    public void getReport() {
        // Save the Extent Report
        ExtentReportManager.getReportInstance().flush();

        // Convert the HTML report to PDF
        ExtentReportManager.convertHtmlToPdf();
    }
    }



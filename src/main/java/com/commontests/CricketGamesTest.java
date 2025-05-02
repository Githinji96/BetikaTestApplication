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
import java.util.Random;


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
    public void selectMultipleCricketGames() {
        try {
            // Click the Cricket button
            cricketbtn.click();
            System.out.println("[INFO] Clicked on Cricket section.");

            // Iterate over all matches and randomly select an odd for each
            Random random = new Random();
            for (WebElement team : teams) {
                List<WebElement> odds = team.findElements(By.className("prebet-match__odd"));

                if (!odds.isEmpty()) {
                    int randomIndex = random.nextInt(odds.size());
                    js.executeScript("arguments[0].click()", odds.get(randomIndex));
                    System.out.println("[INFO] Selected odd at index: " + randomIndex);
                } else {
                    System.out.println("[WARN] No odds available for this match.");
                }
            }

            System.out.println("[INFO] Total matches selected: " + teams.size());

            // Clear and enter amount
            enterAmt.sendKeys(Keys.CONTROL + "a", Keys.BACK_SPACE);
            enterAmt.sendKeys("99");
            System.out.println("[INFO] Entered bet amount: 99");

            // Verify the betslip information
            System.out.println("[INFO] Betslip Details: " + betslip.getText());

            // Get and parse account balance
            String balanceText = accountBalance.getText().replaceAll("[^\\d.]", "").trim();
            double accBalance = Double.parseDouble(balanceText);
            System.out.println("[INFO] Account Balance: " + accBalance);

            // Place bet if balance is sufficient
            if (accBalance >= 99) {
                js.executeScript("arguments[0].click()", submit);
                System.out.println("[INFO] Clicked Submit button.");

                // Wait for success notification
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
                WebElement toast = wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//div[contains(@class, 'notification') and contains(@class, 'show') and contains(@class, 'success')]//div[@class='title']")));

                Assert.assertTrue(toast.getText().contains("Bet Placement Successful"), "[FAIL] Bet placement not successful!");
                System.out.println("[PASS] Bet placed successfully!");
            } else {
                System.out.println("[WARN] Insufficient balance to place bet.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("[FAIL] Test failed due to exception: " + e.getMessage());
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

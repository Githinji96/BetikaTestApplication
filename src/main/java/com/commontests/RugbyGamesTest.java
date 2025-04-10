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
public class RugbyGamesTest {

    WebDriver driver;
    JavascriptExecutor js;
    DriverClass driverClass;

    public String URL;
    public String usernumber;
    public String password;


    @FindBy(xpath="//span[@class='sports-list__item__label narrow'][normalize-space()='Rugby']")
    WebElement rugbybtn;

    @FindBy(xpath = "(//button[normalize-space()='See all upcoming rugby matches'])[1]")
    WebElement clickupcomingmatches;

    @FindAll(@FindBy(className = "prebet-match__odds"))
    List<WebElement> teams;

    @FindBy(xpath="//div[@class='rounded-card']")
    WebElement betslip;

    @FindBy(className = "stacked__odd")
    WebElement oddValue;

    @FindBy(xpath = "//input[@placeholder='Enter stake']")
    WebElement enterAmt;

    @FindBy(className = "betslip__details__row__value")
    WebElement accountBalance;

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
    public void selectRandomlyRugbyGames(){
        js.executeScript("arguments[0].click()",rugbybtn);

        try {
            js.executeScript("arguments[0].click()",clickupcomingmatches);
        } catch (NoSuchElementException e) {

        }
        List<WebElement> odds;
        // Randomly choose team and place  bets
        for (int i = 0; i <=7; i++) {
            int randomTeam = (int) Math.floor(Math.random() * teams.size());
            odds = teams.get(randomTeam).findElements(By.className("prebet-match__odd"));
            int randId = (int) Math.floor(Math.random() * odds.size());
            js.executeScript("arguments[0].click()", odds.get(randId));
            //print either homewin, draw or away win for the matches choosen
            System.out.println(randId);
        }
        System.out.println("Number of matches. " + teams.size());
        driverClass.customWait(driver, 10, enterAmt);

        enterAmt.sendKeys(Keys.CONTROL + "a" + Keys.BACK_SPACE);
        enterAmt.sendKeys("90");

        //Verify the betslip informations
        System.out.println("The betslip "+betslip.getText());


        //get bet balance amount
        String amnt=accountBalance.getText();
        String amnt1 = amnt.replaceAll("KES", "");

        // Parse the remaining string to a double value
        double accBalance = Double.parseDouble(amnt1);
        System.out.println("The account balance "+accBalance);

        //place bet if the account balance amount is greater than 99.
        if(accBalance>=90){
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

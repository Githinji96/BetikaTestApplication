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
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Listeners(com.ListenersPackage.Listeners.class)
public class TestTableTennis {

    WebDriver driver;
    JavascriptExecutor js;
    DriverClass driverClass;

    public String URL;
    public String usernumber;
    public String password;

    @FindBy(xpath = "//span[normalize-space()='Table Tennis']")
    WebElement tennisbtn;

    @FindAll(@FindBy(className = "prebet-match__odd-market__container"))
    public List<WebElement> oddsContainer;

    @FindAll(@FindBy(className = "prebet-match__odds"))
    List<WebElement> teams;

    @FindAll(@FindBy(className ="rounded-card" ))
    public List<WebElement>roundedCard;

    //Amount placeholder
    @FindBy(xpath = "//input[@placeholder='Enter stake']")
    WebElement enterAmt;

    @FindBy(className = "modal__container")
    WebElement modalContainer;

    @FindBy(className = "modal__x")
     WebElement cancelModal;
    @FindBy(xpath = "//button[contains(@class, 'account__payments__submit') and contains(@class, 'betslip__details__button__place')]")
    WebElement submit;



    // Class constructor
    public TestTableTennis() {
        // initializing the pageObjects
        driverClass = new DriverClass();
        driver = driverClass.driver;
        js = driverClass.js;
        PageFactory.initElements(driver, this);
    }

    @BeforeMethod
    private void login() {
        loadProperty ld= new loadProperty();
        try {
            ld.loadProperties();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // Assign the loaded properties to the local instance variables
        this.URL = ld.URL;
        this.usernumber = ld.userNumber;
        this.password = ld.password;

        AppLogin lg = new AppLogin();
        lg.login(URL, usernumber, password, new ArrayList<>(Arrays.asList(driver, js)));
    }

    @Test(retryAnalyzer = rerunFailedTestCases.class)
    public void PlaceTableTennisMatches() {
        js.executeScript("arguments[0].click()",tennisbtn);

        List<WebElement> listItems = oddsContainer;

        // Iterate through the list and print the text of each element
        if (listItems.isEmpty()) {
            System.out.println("No  matches Games Found");
        } else {
            // Iterate through the list and print market games and their odd values
            for (WebElement listItem : listItems) {
                System.out.println("============================================");
                System.out.println(listItem.getText());
            }
        }

        List<WebElement> odds;
        // Randomly choose team and place multiple bets
        for (int i = 0; i <10; i++) {
            int randomTeam = (int) Math.floor(Math.random() * teams.size());
            odds = teams.get(randomTeam).findElements(By.className("prebet-match__odd"));
            int randId = (int) Math.floor(Math.random() * odds.size());
            js.executeScript("arguments[0].click()", odds.get(randId));
            //print either home win or awaywin for the matches choosen
            System.out.println("Bet placed on odd index: " + randId);

        }

        System.out.println("Number of matches. " + teams.size());
        // get all matches selected
        for (WebElement listItem : roundedCard) {
            System.out.println(listItem.getText());
        }
        enterAmt.sendKeys(Keys.CONTROL + "a" + Keys.BACK_SPACE);
        enterAmt.sendKeys("30000");
        // Submit the bets
        js.executeScript("arguments[0].click()", submit);

        //display modal container when insufficient amount is placed.
        Assert.assertTrue(modalContainer.isDisplayed(), "The modal container is not displayed on the page.");

        //cancel the modal after its launched.
        cancelModal.click();

    }

    @AfterTest
    public void teardown(){
        //driver.close();
    }
    @AfterSuite
    public void getReport() {
        // Save the Extent Report
        ExtentReportManager.getReportInstance().flush();

        // Convert the HTML report to PDF
        ExtentReportManager.convertHtmlToPdf();
    }


}

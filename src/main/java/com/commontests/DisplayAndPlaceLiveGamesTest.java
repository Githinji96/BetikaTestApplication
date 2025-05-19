package com.commontests;

import com.PropertyData.loadProperty;
import com.loginpackage.AppLogin;
import com.reRunFailedTests.rerunFailedTestCases;
import com.utils.DriverClass;
import com.utils.ExtentReportManager;
import org.openqa.selenium.*;

import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Listeners(com.ListenersPackage.Listeners.class)
public class DisplayAndPlaceLiveGamesTest {
    WebDriver driver;
    JavascriptExecutor js;
    DriverClass driverClass;

    public String URL;
    public String liveurl;
    public String usernumber;
    public String password;

    @FindAll(@FindBy(className = "live-match__odds"))
    List<WebElement> teams;

    @FindBy(xpath = "//button[normalize-space()='Remove Expired']")
    WebElement expiredgames;

    @FindBy(className = "betslip__details__button__place")
    WebElement acceptAndPlacebet;

    @FindAll(@FindBy(className = "stacked"))
    List<WebElement> listGames;


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
        loadProperty ld = new loadProperty();
        ld.loadProperties();
        // Assign the loaded properties to the local instance variables
        this.URL = ld.URL;
        this.usernumber = ld.userNumber;
        this.password = ld.password;

        //Lauch login
        AppLogin lg = new AppLogin();
        lg.login(URL, usernumber, password, new ArrayList<>(Arrays.asList(driver, js)));

    Set<Cookie> cookies = driver.manage().getCookies();
        for(Cookie cookie :cookies) {
        if (cookie.getDomain().contains("betika.com")) { // Check if the cookie is for Betika
            driver.manage().addCookie(cookie);
        }
    }
}


    @Test(retryAnalyzer = rerunFailedTestCases.class)
    public void testlivegames() throws IOException {
        loadProperty ld= new loadProperty();
        ld.loadProperties();
        this.liveurl = ld.liveUrl;
        driver.get(liveurl);
        java.util.List<WebElement> games = driver.findElements(By.className("live__matches"));

        // Loop through each WebElement and print the text
        //print the live games in session.

        if (games.isEmpty()) {
            System.out.println("No live games availabe");
        } else {

            for (WebElement game : games) {
                System.out.println(game.getText());
            }
        }
           // Randomly select multiple live games
        List<WebElement> odds;
        // Randomly choose team and place multiple bets
        for (int i = 0; i < 10; i++) {
            int randomTeam = (int) Math.floor(Math.random() * teams.size());
            odds = teams.get(randomTeam).findElements(By.className("live-match__odd"));
            int randId = (int) Math.floor(Math.random() * odds.size());
            js.executeScript("arguments[0].click()", odds.get(randId));

        }
        //submit to place the live bets
        try{
             //click to remove expired games
            js.executeScript("arguments[0].click()", expiredgames);
        }catch (NoSuchElementException e){

        }
         //  get the list of live games on the betslip to be placed
        List<WebElement> listItems = listGames;
        for (WebElement listItem : listItems) {
            System.out.println("=================================");
            String gameslist=listItem.getText();
            if(gameslist.isEmpty()){
                System.out.println("No live games added in betslip");
            }else {
                System.out.println(gameslist);
            }

        }
        js.executeScript("arguments[0].click()",acceptAndPlacebet);

    }
    @AfterSuite
    public void getReport() {
        // Save the Extent Report
        ExtentReportManager.getReportInstance().flush();

        // Convert the HTML report to PDF
        ExtentReportManager.convertHtmlToPdf();
    }

}
package com.commontests;

import com.PropertyData.loadProperty;
import com.loginpackage.AppLogin;
import com.utils.DriverClass;
import com.utils.ExtentReportManager;
import org.openqa.selenium.*;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Listeners(com.ListenersPackage.Listeners.class)
public class CasinoGamesTest {
    WebDriver driver;
    JavascriptExecutor js;
    DriverClass driverClass;

    public String URL;
    public String usernumber;
    public String password;


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
        Set<Cookie> cookies = driver.manage().getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getDomain().contains("betika.com")) { // Check if the cookie is for Betika
                driver.manage().addCookie(cookie);
            }
        }
    }

    @Test
    public void casinoTest(){

        driver.get("https://www.betika.com/en-ke/casino");

        List<WebElement> listItems = driver.findElements(By.xpath("//div[@class='casino__games']"));

        // Iterate through the list and print the text of each element
        if (listItems.isEmpty()) {
            System.out.println("No Casino Games Found");
        } else {
            // Iterate through the list and print the text of each element
            for (WebElement listItem : listItems) {
                System.out.println("============================================");
                System.out.println(listItem.getText());
            }
        }
        WebElement SecondcasinoGame=driver.findElement(By.xpath("(//div[@class='casino-game__img'])[2]"));
        Assert.assertTrue(SecondcasinoGame.isDisplayed(), "Second casino game is not displayed!");
        SecondcasinoGame.click();

        //launch extra juicy casino game
        WebElement elembtn=driver.findElement(By.xpath("//button[normalize-space()='Play Game']"));
        Assert.assertTrue(elembtn.isEnabled(), "Button not clickable!");
        elembtn.click();

    }
    @AfterMethod
    public void teardown(){
      //  driver.quit();
    }
    @AfterSuite
    public void getReport() {
        // Save the Extent Report
        ExtentReportManager.getReportInstance().flush();

        // Convert the HTML report to PDF
        ExtentReportManager.convertHtmlToPdf();
    }
}

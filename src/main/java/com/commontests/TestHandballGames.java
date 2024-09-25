package com.commontests;

import com.PropertyData.loadProperty;
import com.reRunFailedTests.rerunFailedTestCases;
import com.loginpackage.AppLogin;
import com.utils.DriverClass;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestHandballGames {
    WebDriver driver;
    JavascriptExecutor js;
    DriverClass driverClass;

    public String URL;
    public String usernumber;
    public String password;


    @FindBy(xpath = "(//div[@class='sports-list__item'])[5]")
    WebElement handballbtn;

    @FindBy(xpath="//button[normalize-space()='See all upcoming rugby matches']")
    WebElement upcomingmatches;

    @FindBy(xpath="//a[@class='prebet-match__markets'][1]")
    WebElement clickmarkets;

    @FindBy(className = "stacked__details")
    WebElement printselectedOption;

    public TestHandballGames(){
        // initializing the pageObjects
        driverClass = new DriverClass();
        driver = driverClass.driver;
        js = driverClass.js;//
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
        this.usernumber = ld.usernumber;
        this.password = ld.password;

        AppLogin lg = new AppLogin();
        lg.login(URL, usernumber, password, new ArrayList<>(Arrays.asList(driver, js)));

    }

    @Test(retryAnalyzer = rerunFailedTestCases.class)
    public void selectSingleRandomBet() throws InterruptedException {
       handballbtn.click();

        try {
            js.executeScript("arguments[0].click()", upcomingmatches);
        } catch (NoSuchElementException e) {
            System.out.println("Upcoming matches button not found, proceeding with available matches.");
        }

        //upcomingmatches.click();
        clickmarkets.click();

        List<WebElement> options = driver.findElements(By.className("market__odds"));

        int rSize = (int) Math.floor(Math.random()*options.size());

        List<WebElement> oddButtons = options.get(rSize)
                .findElements(By.className("odd"));

        js.executeScript("arguments[0].click()",
               oddButtons.get((int) Math.floor(Math.random()*oddButtons.size()))
        );
        //get the randomly selected option bet
         System.out.println(printselectedOption.getText());
        System.out.println(rSize);
    }
}

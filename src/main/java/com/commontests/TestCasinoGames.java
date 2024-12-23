package com.commontests;

import com.PropertyData.loadProperty;
import com.loginpackage.AppLogin;
import com.utils.DriverClass;
import org.openqa.selenium.*;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class TestCasinoGames {
    WebDriver driver;
    JavascriptExecutor js;
    DriverClass driverClass;

    public String URL;
    public String usernumber;
    public String password;

    public TestCasinoGames() {
        // initializing the pageObjects
        driverClass = new DriverClass("chrome");
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

    @Test
    public void casinoTest(){

        Set<Cookie> cookies = driver.manage().getCookies();
        driver.get("https://www.betika.com/en-ke/casino");
        for (Cookie cookie : cookies) {
            if (cookie.getDomain().contains("betika.com")) { // Check if the cookie is for Betika
                driver.manage().addCookie(cookie);
            }
        }
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

        WebElement elembtn=driver.findElement(By.xpath("//button[normalize-space()='Play Game']"));
        Assert.assertTrue(elembtn.isEnabled(), "Button not clickable!");
        elembtn.click();

    }
}

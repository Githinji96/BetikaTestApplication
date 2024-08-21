package com.commontests;

import com.loginpackage.AppLogin;
import com.utils.DriverClass;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestAussieRules {

    WebDriver driver;
    JavascriptExecutor js;
    DriverClass driverClass;
    public String URL = "https://www.betika.com/en-ke/login";


    @FindBy(xpath="//span[@class='sports-list__item__label narrow'][normalize-space()='Aussie Rules']")
    WebElement aussieRulebtn;

    @FindBy(xpath = "//button[normalize-space()='See all upcoming aussie rules matches']")
    WebElement clickupcomingmatches;

    @FindAll(@FindBy(className = "prebet-match__odds"))
    List<WebElement> teams;

    public TestAussieRules(){

            // initializing the pageObjects
            driverClass = new DriverClass();
            driver = driverClass.driver;
            js = driverClass.js;
            PageFactory.initElements(driver, this);
    }
    @BeforeMethod
    private void login() {
        AppLogin lg = new AppLogin();
        lg.login(URL, "0711198013", "Bg33173375", new ArrayList<>(Arrays.asList(driver, js)));
    }
    @Test
    public void selectRandomlyAussieGames(){
        aussieRulebtn.click();
        try {
            clickupcomingmatches.click();
        } catch (NoSuchElementException e) {
            System.out.println("Upcoming matches button not found, proceeding with available matches.");
        }
        List<WebElement> odds;
        // Randomly choose team and place 4 bets
        for (int i = 0; i <10; i++) {
            int randomTeam = (int) Math.floor(Math.random() * teams.size());
            odds = teams.get(randomTeam).findElements(By.className("prebet-match__odd"));
            int randId = (int) Math.floor(Math.random() * odds.size());
            js.executeScript("arguments[0].click()", odds.get(randId));
            //print either homewin, draw or awaywin for the matches choosen
            System.out.println(randId);
        }
        System.out.println("Number of matches. " + teams.size());
    }
}

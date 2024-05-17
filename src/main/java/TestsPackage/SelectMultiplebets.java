package baseClassPackage;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.List;

public class SelectMultiplebets {
    WebDriver driver;
    JavascriptExecutor js;

    public String URL = "https://www.betika.com/en-ke";

    //placebet button
    @FindBy(xpath = "//button[contains(@class, 'account__payments__submit') and contains(@class, 'betslip__details__button__place')]")
    WebElement submit;

    @FindAll(@FindBy(className = "prebet-match__odds"))
    List<WebElement> teams;

    public SelectMultiplebets() {
        String browser = "chrome";
        DriverClass driverClass = new DriverClass(browser);
        driver = driverClass.driver;
        js = driverClass.js;
        PageFactory.initElements(driver, this);

    }

    @BeforeTest
    public void visitUrl() {
        driver.get(URL);
    }

    @Test
    public void placeBets() {
        // Home, draw or away buttons
        List<WebElement> odds=null;
        // Randomly choose team and place  bets
        for (int i = 0; i < 20; i++) {
            int randomTeam = (int) Math.floor(Math.random() * teams.size());
            odds = teams.get(randomTeam).findElements(By.className("prebet-match__odd"));
            int randId = (int) Math.floor(Math.random() * odds.size());
            js.executeScript("arguments[0].click()", odds.get(randId));
           // System.out.println(randId);
        }
        System.out.println(odds.size());
        System.out.println(teams.size());
        // Submit the bets
      //  js.executeScript("arguments[0].click()", submit);
    }

    @AfterTest
    public void tearDown() {

    }
}

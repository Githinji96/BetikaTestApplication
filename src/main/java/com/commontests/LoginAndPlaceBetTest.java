package com.commontests;

import com.loginpackage.AppLogin;
import com.utils.DriverClass;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;

public class SomeClass {
    WebDriver driver;
    JavascriptExecutor js;
    public String URL = "https://www.betika.com/en-ke/login";

    // page factory
    //phone number login locator
    @FindBy(xpath = "//input[@placeholder='e.g. 0712 234567']")
    WebElement phoneNumber;
    // passwordlocator
    @FindBy(xpath = "//input[@type='password']")
    WebElement password;
    //login button
    @FindBy(xpath = "//button[@class='button account__payments__submit session__form__button login button button__secondary']")
    WebElement loginBtn;

    @FindBy(xpath = "//span[normalize-space()='My Bets']")
    WebElement profile;

    //upcomingbtn
    @FindBy(className = "prematch-nav__item")
    WebElement upcomingbtn;

    // upcoming daysbtn
    @FindBy(className = "match-filter__button")
    WebElement weekbtn;

    @FindBy(xpath = "//button[normalize-space()='Saturday']")
    WebElement saturday;
    //highlight matches btn
    @FindBy(xpath = "//button[@class='match-filter__group__action active'][normalize-space()='Highlights']")
    WebElement highlight;
    // click apply
    @FindBy(className = "match-filter__apply")
    WebElement apply;

    @FindBy(className = "prebet-match__odd")
    WebElement clickwin;

    //Amount placeholder
    @FindBy(xpath = "//input[@placeholder='Enter stake']")
    WebElement enterAmt;
    //placebet button
    @FindBy(xpath = "//button[contains(@class, 'account__payments__submit') and contains(@class, 'betslip__details__button__place')]")
    WebElement submit;

    // Class constructor
    public SomeClass() {
        // initializing the pageObjects
        DriverClass driverClass = new DriverClass("chrome");
        driver = driverClass.driver;
        js = driverClass.js;
        PageFactory.initElements(driver, this);
    }

    @Test
    public void placebet() {
        AppLogin lg = new AppLogin();

        lg.login(URL, "0711198013", "Bg33173375", new ArrayList<>(Arrays.asList(driver, js)));
        String title = driver.getTitle();
        Assert.assertEquals(title, "Betika | Best Online Sports Betting in Kenya");
        //Assert user successful login by viewing my bet page
        WebElement element = driver.findElement(By.xpath("//span[normalize-space()='My Bets']"));
        boolean isElementDisplayed = element.isDisplayed();
        boolean isElementEnabled = element.isDisplayed();

        if (isElementDisplayed && isElementEnabled) {
            System.out.println("Element is visible");
        } else {
            System.out.println("Eelement is not displayed");
        }
        upcomingbtn.click();
        //select day
        weekbtn.click();
        saturday.click();
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.visibilityOf(highlight));
        highlight.click();
        apply.click();

        WebElement elem = driver.findElement(By.xpath("//div[@class='selected-filters__item']"));
        boolean isElement_Displayed = elem.isDisplayed();
        boolean isElement_Enabled = elem.isEnabled();

        Assert.assertTrue(isElement_Displayed && isElement_Enabled, "Game day not found");

        clickwin.click();
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.visibilityOf(enterAmt));
        enterAmt.click();
        enterAmt.sendKeys("10");
        js.executeScript("arguments[0].click()", submit);
        //notification-show success > title
        WebElement toast = driver.findElement(
                By.xpath("//div[contains(@class, 'notification') and contains(@class, 'show') and contains(@class, 'success')]//div[@class='title']"));
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.visibilityOf(toast));
        Assert.assertTrue(toast.getText().contains("Bet Placement Successful"));

    }


}

package com.commontests;

import com.utils.DriverClass;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import java.time.Duration;

public class ZerakiTest {
    WebDriver driver;
    JavascriptExecutor js;
    DriverClass driverClass;

    @FindBy(xpath="//div[@class='normal-input input-group']//input[@id='phone']")
    WebElement username;

    @FindBy(xpath="//button[@class='btn btn-block btn-success text-white col-12 text-center']")
    WebElement continuebtn;

    @FindBy(xpath="(//input[@id='undefined'])[1]")
    WebElement enterpass;

    @FindBy(xpath="//button[@class='btn btn-success text-white text-center col-12']")
    WebElement contbtn;

    @FindBy(xpath="//i[@class='icon-Search']//span[@class='path1']")
    WebElement search;

    @FindBy(xpath="//input[@placeholder='Admission Number']")
    WebElement searchinput;

    @FindBy(xpath="//button[normalize-space()='Search']")
    WebElement searchbtn;

    @FindBy(xpath="//span[normalize-space()='Analytics']")
    WebElement analytbtn;

    @FindBy(xpath="//button[normalize-space()='Report Forms']")
    WebElement reportbtn;

    @FindBy(xpath="//div[@class='dropdown-menu show']//a[@class='dropdown-item'][normalize-space()='Current Student']")
    WebElement clickbtn;

    @FindBy(xpath = "//button[normalize-space()='Download']")
    WebElement downloadbtn;

    public ZerakiTest() {
        // initializing the pageObjects
        driverClass = new DriverClass("chrome");
        driver = driverClass.driver;
        js = driverClass.js;
        PageFactory.initElements(driver, this);
    }
    @Test
    public void TestDownload() throws InterruptedException {
      driver.get("https://analytics.zeraki.app/");
      username.sendKeys(" admin@verify ");
      continuebtn.click();
      enterpass.sendKeys("helloo");
      contbtn.click();

      //search
        search.click();
        searchinput.sendKeys("34");
        searchbtn.click();
        analytbtn.click();

        Thread.sleep(4000);
        // Locate the element



// Create a JavascriptExecutor instance
        JavascriptExecutor js = (JavascriptExecutor) driver;

// Scroll to the specific element
      //  js.executeScript("arguments[0].scrollIntoView(true);", reportbtn);

// Perform the click action after scrolling
        driverClass.customWait(driver, 5, reportbtn);
        WebElement element = driver.findElement(By.xpath("//button[normalize-space()='Report Forms']"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        js.executeScript("arguments[0].click()",element);

        //  reportbtn.click();

        js.executeScript("arguments[0].click()",clickbtn);
        // click download
        Thread.sleep(4000);
        js.executeScript("arguments[0].click()",downloadbtn);

    }

}

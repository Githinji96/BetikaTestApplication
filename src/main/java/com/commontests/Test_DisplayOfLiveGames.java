package com.commontests;

import com.PropertyData.loadProperty;
import com.utils.DriverClass;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;

public class Test_DisplayOfLiveGames {
    WebDriver driver;
    JavascriptExecutor js;
    DriverClass driverClass;

    public String liveurl;

    public Test_DisplayOfLiveGames() {
        driverClass = new DriverClass("chrome");
        driver = driverClass.driver;
        js = driverClass.js;
        PageFactory.initElements(driver, this);

    }
    @BeforeMethod
    private void visitLiveUrl() {
        loadProperty ld= new loadProperty();
        try {
            ld.loadProperties();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // Assign the loaded properties to the local instance variables
        this.liveurl = ld.liveUrl;

        //navigate to the live games link
       driver.get(liveurl);


    }
   @Test
    public void testlive(){
        java.util.List<WebElement> games = driver.findElements(By.className("live__matches"));

      // Loop through each WebElement and print the text
        //print the live games in session.
        for (WebElement game : games) {
         System.out.println(game.getText());

        }

    }
}
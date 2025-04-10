package com.commontests;

import com.PropertyData.loadProperty;
import com.randompackage.GenerateRandomData;
import com.utils.DriverClass;
import com.utils.ExtentReportManager;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.*;

import java.io.IOException;
import java.util.List;

@Listeners(com.ListenersPackage.Listeners.class)
public class RegisterUserTest {
    private WebDriver driver;
    private JavascriptExecutor js;
    private DriverClass driverClass;
    private String betUrl;

    // Register link
    @FindBy(linkText = "Register")
    private WebElement register;

    // Phone number text field
    @FindBy(xpath = "//input[@placeholder='e.g. 0712 234567']")
    private WebElement phoneNumber;

    // Password text fields
    @FindAll(@FindBy(xpath = "//input[@type='password']"))
    private List<WebElement> passwordFields;

    // Accept terms radio button
    @FindBy(className = "checkmark")
    private WebElement acceptTerms;

    // Submit button
    @FindBy(xpath = "//button[contains(@class, 'button') and contains(@class, 'account__payments__submit') and contains(@class, 'session__form__button') and contains(@class, 'login') and contains(@class, 'button') and contains(@class, 'button__secondary')]")
    private WebElement submitBtn;

    @BeforeTest
    public void setup() {
        try {
            System.out.println("Initializing WebDriver...");
            driverClass = new DriverClass("Chrome");
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
    public void visitUrl() throws IOException {
        if (driver == null) {
            throw new RuntimeException("WebDriver is not initialized");
        }
        loadProperty ld = new loadProperty();
        ld.loadProperties();
        this.betUrl = ld.betUrl;
        driver.get(betUrl);
    }

    @AfterTest
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void registerUser() {
        register.click();
        String password = GenerateRandomData.randomName(8);
        phoneNumber.clear();
        phoneNumber.sendKeys(GenerateRandomData.randomNum());

        for (WebElement field : passwordFields) {
            field.sendKeys(password);
        }
        js.executeScript("arguments[0].click()", acceptTerms);
        js.executeScript("arguments[0].click()", submitBtn);
    }

    @AfterSuite
    public void getReport() {
        // Save the Extent Report
        ExtentReportManager.getReportInstance().flush();

        // Convert the HTML report to PDF
        ExtentReportManager.convertHtmlToPdf();
    }
}

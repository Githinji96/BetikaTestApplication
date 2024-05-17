package baseClassPackage;


import java.time.Duration;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeSuite;

import io.github.bonigarcia.wdm.WebDriverManager;

public class DriverClass {

	public String browser;
	public WebDriver driver;
	public WebDriverWait wait;
	public JavascriptExecutor js;

	// browser variable holds the name of the browser
	public DriverClass(String browser) {
		this.browser = browser;
		setup();
	}

	// default browser-no-con fig
	public DriverClass() {
		this.browser = "chrome";
		setup();
	}
	@BeforeSuite
	@SuppressWarnings("deprecation")
	public void setup() {
		if (browser.equalsIgnoreCase("firefox")) {
			WebDriverManager.firefoxdriver().setup();
			driver = new FirefoxDriver();
		} else if (browser.equalsIgnoreCase("chrome")) {
			WebDriverManager.chromedriver().setup();
			driver = new ChromeDriver();
		} else if (browser.equalsIgnoreCase("Edge")) {
			WebDriverManager.edgedriver().setup();
			driver = new EdgeDriver();
		}
		js = (JavascriptExecutor) driver;
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.manage().deleteAllCookies();
		driver.manage().window().maximize();

		wait =  new WebDriverWait(driver, Duration.ofSeconds(5));
	}
}

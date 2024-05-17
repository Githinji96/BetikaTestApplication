package baseClassPackage;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


public class TestBrokenLinks {

	String browser = "chrome";
	WebDriver driver;
	String Homepage = "https://www.betika.com/en-ke/";
	private DriverClass driverClass;
	private JavascriptExecutor js;

	public TestBrokenLinks() {
		// initializing the pageObjects
		DriverClass driverClass = new DriverClass("chrome");
		driver = driverClass.driver;
		String browser = "chrome";
		PageFactory.initElements(driver, this);

	}

	@BeforeMethod
	public void visitURL() {

		driver.get(Homepage);
	}

	@AfterTest
	public void tearDown() {
		driver.quit();
		driver = null;
	}

	@Test
	public void testBrokenLinks() {
		String url = "";
		HttpURLConnection huc = null;
		int respCode = 200;
		List<WebElement> links = driver.findElements(By.tagName("a"));

		Iterator<WebElement> it = links.iterator();

		while (it.hasNext()) {

			url = it.next().getAttribute("href");
			System.out.println(url);
			if (url == null || url.isEmpty()) {
				System.out.println("URL is either not configured for anchor tag or it is empty");
				continue;
			}

			if (!url.startsWith(Homepage)) {
				System.out.println("URL belongs to another domain, skipping it.");
				continue;
			}
			try {
				huc = (HttpURLConnection) (new URL(url).openConnection());

				huc.setRequestMethod("HEAD");

				huc.connect();
				if (respCode >= 400) {
					System.out.println(url + " is a broken link");
				} else {
					System.out.println(url + " is a valid link");
				}

			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}

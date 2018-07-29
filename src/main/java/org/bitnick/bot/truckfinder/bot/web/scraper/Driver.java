package org.bitnick.bot.truckfinder.bot.web.scraper;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

public class Driver {
	public WebDriver driver;
	
	private Driver() {
		setUp();
	}
	
	private void setUp() {
		System.setProperty("webdriver.chrome.driver", new File(".").getAbsolutePath() + "/chromedriver.exe");
		
        ChromeOptions options = new ChromeOptions();
        //options.addArguments("headless");

		this.driver = new ChromeDriver(options);
		
		this.driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
	}
	
	public void tearDown() {
		this.driver.quit();
		this.driver = null;
	}
	
	public void navigateTo(String targetUrl) throws Exception {
		driver.get(targetUrl);
	}
	
	public void click(String xpath) throws Exception {
		getElement(xpath).click();
	}
	
	public WebElement getElement(String xpath) {
		return driver.findElement(By.xpath(xpath));
	}
	
	public List<WebElement> getElements(List<String> xpaths) throws Exception {
		return xpaths.stream().map(xpath -> getElement(xpath)).collect(Collectors.toList());
	}
	
	public static Driver instanceOf() { 
		return new Driver();
	}
}

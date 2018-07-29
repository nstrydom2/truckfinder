package org.bitnick.bot.truckfinder.bot.web.scraper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bitnick.bot.truckfinder.database.model.Truck;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Component;

import static org.openqa.selenium.support.ui.ExpectedConditions.*;

@Component
public class WebScraper {
	private final Driver driver = Driver.instanceOf();
	
	private WebScraper() {
		
	}
	
	private List<Truck> combineList(List<Truck> l1, List<Truck> l2) {
		return Stream.concat(l1.stream(), l2.stream()).collect(Collectors.toList());
	}
	
	private List<Truck> sortByPrice(List<Truck> targetList) {
		return targetList.stream().sorted(Comparator.comparingDouble(Truck::getPrice)).collect(Collectors.toList());
	}
	
	private Truck mapToTruck(WebElement result, String website) {
		Truck truck = new Truck();
		truck.setWebsite(website);
		truck.setPrice(Double.valueOf(result.getText().replace("$","")));
		
		return truck;
	}
	
	public List<Truck> getAllResults(String zipcode, String pickUpDate, String pickUpTime, String truckSize) throws Exception {
		try {
			String[] arr = pickUpDate.split("/");
			
			List<Truck> results = combineList( 
					combineList(
							getUhaulResults(zipcode, pickUpDate, pickUpTime, truckSize), 
							getPenskeResults(zipcode, pickUpDate, pickUpTime, truckSize)
							),
					getBudgetResults(zipcode, arr[2] + "-" + arr[0] + "-" + arr[1], pickUpTime, truckSize)
					);
					
			return sortByPrice(results);
		}
		
		finally {
			driver.tearDown();
		}
	}
	
	public List<Truck> getUhaulResults(String zipcode, String pickUpDate, String pickUpTime, String truckSize) throws Exception {
		// Weird hack for bug that came out of nowhere
		//driver.driver.switchTo().window(driver.windowHandle);
		
		// base info
		String websiteName = "Uhaul";
		String websiteUrl = "https://uhaul.com/";
		
		// Web element xpaths
		String pickUpLocationXpath = "//*[@id=\"PickupLocation-TruckOnly\"]";	// Field located on front page
		String pickUpDateXpath = "//*[@id=\"PickupDate-TruckOnly\"]";
		String pickUpTimeXpath = "//*[@id=\"PickupTime-TruckOnly\"]";
		String submitButtonXpath = "/html/body/main/div/div/div/section/div[1]/div[1]/form/div[3]/div[3]/button";
		
		driver.navigateTo(websiteUrl);
		driver.getElement(pickUpLocationXpath).sendKeys(zipcode);	// send input to fields
		driver.getElement(pickUpDateXpath).sendKeys(pickUpDate);
		driver.getElement(pickUpTimeXpath).sendKeys(pickUpTime);
		driver.click(submitButtonXpath);	// second page click on proper truck size
		
		List<String> xpaths = new ArrayList<String>();
	
		for (int count = 1; count <= 7; count++) {
			String truckSizeXpath = "/html/body/main/div/div/div/section/div[3]/ul/li["+ String.valueOf(count) +"]/div/div[1]/div/div[1]/h3";
			String priceXpath = "/html/body/main/div/div/div/section/div[3]/ul/li["+ String.valueOf(count) +"]/div/div[2]/div/form/div/div[2]/dl/dd/div[1]/b";
			
			if (driver.getElement(truckSizeXpath).getText().contains(truckSize))
				xpaths.add(priceXpath);
		}
		
		List<WebElement> results = driver.getElements(xpaths);
		
		return results.stream().map(result -> mapToTruck(result, websiteName)).collect(Collectors.toList());

	}
	
	public List<Truck> getPenskeResults(String zipcode, String pickUpDate, String pickUpTime, String truckSize) throws Exception {
		String websiteName = "Penske";
		String websiteUrl = "https://www.pensketruckrental.com/quote/start.html";
		
		String pickUpLocationXpath = "//*[@id=\"pickup_location_txtbox\"]";
		String pickUpDateXpath = "//*[@id=\"pickupdate\"]";
		String submitButtonXpath = "/html/body/app-root/ng-component/div/section/form/div[2]/div/button";
		//*[@id="returnto"]
		String returnToSamePlaceXpath = "/html/body/app-root/ng-component/div/section/form/fieldset[3]/div/div[1]/div/label";
		String bypassButtonXpath = "/html/body/app-root/app-pickup-location/div/section/div[3]/div/div/app-carousal[1]/div[2]/a";	// Just selects closest location
		String secondBypassXpath = "/html/body/app-root/app-pickup-location/div/section/div[4]/div/button";
		String finalBypassXpath = "/html/body/app-root/ng-component/div[2]/section/div[1]/div/form/div[2]/div/button";
		
		List<String> xpaths = new ArrayList<String>();

		driver.navigateTo(websiteUrl);
		driver.click(returnToSamePlaceXpath);
		driver.getElement(pickUpLocationXpath).sendKeys(zipcode);	// send input to fields
		driver.getElement(pickUpDateXpath).sendKeys(pickUpDate);
		driver.click(submitButtonXpath);	// second page click on proper truck size
		driver.click(bypassButtonXpath);
		
		Thread.sleep(1000);
		driver.click(secondBypassXpath);
		
		Thread.sleep(1000);
		driver.click(finalBypassXpath);
		
		for (int count = 1; count <= 3; count++) {
			String truckSizeXpath = "/html/body/app-root/ng-component/div[2]/section/div[1]/div[1]/div[2]/div/div[" + String.valueOf(count) + "]/div[1]/h4";
			String priceXpath = "/html/body/app-root/ng-component/div[2]/section/div[1]/div[1]/div[2]/div/div[" + String.valueOf(count) + "]/div[1]/span[1]";
			
			if (driver.getElement(truckSizeXpath).getText().contains(truckSize))
				xpaths.add(priceXpath);
		}
		
		List<WebElement> results = driver.getElements(xpaths);
		
		return results.stream().map(result -> mapToTruck(result, websiteName)).collect(Collectors.toList());
	}
	
	public List<Truck> getEnterpriseResults(String zipcode, String pickUpDate, String pickUpTime, String truckSize) throws Exception {
		String websiteName = "Enterprise";
		String websiteUrl = "http://uhaul.com/";
		
		String pickUpLocationXpath = "//*[@id=\"txtPickupLocation\"]";
		String pickUpDateXpath = "";
		String pickUpTimeXpath = "//*[@id=\"dnn_ctr12812_View_ddlPickupTimes\"]";
		String noReturnLocation = "//*[@id=\"radio-value-LO\"]";
		String submitButtonXpath = "//*[@id=\"btnFindTruck\"]";
		
		List<String> xpaths = new ArrayList<String>();
		
		driver.navigateTo(websiteUrl);
		driver.getElement(pickUpLocationXpath).sendKeys(pickUpDate);;
		driver.getElement(pickUpDateXpath).sendKeys(pickUpDate);;
		driver.getElement(pickUpTimeXpath).sendKeys("pickUpTime");;
		driver.click(noReturnLocation);
		driver.click(submitButtonXpath);	// xpath is parameter

		for (int count = 1; count <= 7; count++) {
			String truckSizeXpath = "/html/body/main/div/div/div/section/div[3]/ul/li["+ String.valueOf(count) +"]/div/div[1]/div/div[1]/h3";
			
			if (driver.getElement(truckSizeXpath).getText().contains(truckSize))
				xpaths.add("/html/body/main/div/div/div/section/div[3]/ul/li["+ String.valueOf(count) +"]/div/div[2]/div/form/div/div[2]/dl/dd/div[1]/b");
		}
		
		
		List<WebElement> results = driver.getElements(xpaths);
		
		return results.stream().map(result -> mapToTruck(result, websiteName)).collect(Collectors.toList());
	}
	
	public List<Truck> getBudgetResults(String zipcode, String pickUpDate, String pickUpTime, String truckSize) throws Exception {
		String websiteName = "Budget";
		String websiteUrl = "https://www.budgettruck.com/";
		
		String pickUpLocationXpath = "//*[@id=\"txtPickupLocation\"]";
		String pickUpDateOpenCalendar = "//*[@id=\"pickUpDate\"]";
		String pickUpDateCssClass = "date-" + pickUpDate;
		String pickUpTimeXpath = "//*[@id=\"dnn_ctr12812_View_ddlPickupTimes\"]";
		String noReturnLocation = "//*[@id=\"radio-value-LO\"]";
		String submitButtonXpath = "//*[@id=\"btnFindTruck\"]";
		
		List<String> xpaths = new ArrayList<String>();

		driver.navigateTo(websiteUrl);
		driver.click(noReturnLocation);
		driver.getElement(pickUpLocationXpath).sendKeys(zipcode);
		driver.getElement(pickUpDateOpenCalendar).click();
		
		WebDriverWait wait = new WebDriverWait(driver.driver, 5);
		
		wait.until(visibilityOfElementLocated(By.xpath("//*[@id=\"datepicker-popup-header-pickUpDate\"]")));
		//driver.getElement("//*[@id=\"pickUpDate\"]").sendKeys(pickUpDate);
		driver.click("/html/body/div[4]/div[2]/div/div[1]/table/tbody/tr[5]/td[1]/a");
		
		driver.getElement(pickUpTimeXpath).sendKeys(pickUpTime);
		driver.click(submitButtonXpath);	// xpath is parameter

		for (int count = 1; count <= 4; count++) {
			String truckSizeXpath = "/html/body/form/div[8]/div[2]/div/div[2]/div/div/div/div/div[10]/div[" + String.valueOf(count) + "]/div/div[2]/div[2]/h2";
			String priceXpath = "/html/body/form/div[8]/div[2]/div/div[2]/div/div/div/div/div[10]/div["  + String.valueOf(count) + "]/div/div[3]/div[2]/span[1]";
			
			if (driver.getElement(truckSizeXpath).getText().contains(truckSize))
				xpaths.add(priceXpath);
		}
		
		
		List<WebElement> results = driver.getElements(xpaths);
		
		return results.stream().map(result -> mapToTruck(result, websiteName)).collect(Collectors.toList());
	}
	
	public static WebScraper instanceOf() {
		return new WebScraper();
	}
}

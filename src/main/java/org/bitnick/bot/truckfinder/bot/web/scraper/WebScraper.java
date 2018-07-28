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
import org.springframework.stereotype.Component;

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
		truck.setPrice(Double.valueOf(result.getText()));
		
		return truck;
	}
	
	public List<Truck> getUhaulResults(String zipcode, String pickUpDate, String pickUpTime, String truckSize) throws Exception {
		// base info
		String websiteName = "Uhaul";
		String websiteUrl = "http://uhaul.com/";
		
		// Web element xpaths
		String pickUpLocationXpath = "//*[@id=\"PickupLocation-TruckOnly\"]";	// Field located on front page
		String pickUpDateXpath = "//*[@id=\"PickupDate-TruckOnly\"]";
		String pickUpTimeXpath = "//*[@id=\"PickupTime-TruckOnly\"]";
		String submitButtonXpath = "/html/body/main/div/div/div/section/div[1]/div[1]/form/div[3]/div[3]/button";
		
		try {
			driver.navigateTo(websiteUrl);
			driver.getElement(pickUpLocationXpath).sendKeys(zipcode);	// send input to fields
			driver.getElement(pickUpDateXpath).sendKeys(pickUpDate);
			driver.getElement(pickUpTimeXpath).sendKeys(pickUpTime);
			driver.click(submitButtonXpath);	// second page click on proper truck size
			
			List<String> xpaths = new ArrayList<String>();
		
			for (int count = 1; count <= 7; count++) {
				String truckSizeXpath = "/html/body/main/div/div/div/section/div[3]/ul/li["+ String.valueOf(count) +"]/div/div[1]/div/div[1]/h3";
				
				if (driver.getElement(truckSizeXpath).getText().contains(truckSize))
					xpaths.add("/html/body/main/div/div/div/section/div[3]/ul/li["+ String.valueOf(count) +"]/div/div[2]/div/form/div/div[2]/dl/dd/div[1]/b");
			}
			
			List<WebElement> results = driver.getElements(xpaths);
			
			return results.stream().map(result -> mapToTruck(result, websiteName)).collect(Collectors.toList());
		}
		
		finally {
			driver.tearDown();
		}
	}
	
	public List<Truck> getPenskeResults(String zipcode, String pickUpLocation, String pickUpDate) throws Exception {
		String websiteName = "Penske";
		String websiteUrl = "https://www.pensketruckrental.com/quote/#/start.htm";
		
		String pickUpLocationXpath = "//*[@id=\"pickup_location_txtbox\"]";
		String pickUpDateXpath = "//*[@id=\"pickupdate\"]";
		String submitButtonXpath = "/html/body/app-root/ng-component/div/section/form/div[2]/div/button";
		
		String bypassButtonXpath = "/html/body/app-root/app-pickup-location/div/section/div[3]/div/div/app-carousal[1]/div[2]/a";	// Just selects closest location
		String secondBypassXpath = "/html/body/app-root/app-pickup-location/div/section/div[4]/div/button";
		
		List<String> xpaths = new ArrayList<String>();
		
		try {
			driver.navigateTo(websiteUrl);
			driver.getElement(pickUpLocationXpath).sendKeys(zipcode);	// send input to fields
			driver.getElement(pickUpDateXpath).sendKeys(pickUpDate);
			driver.click(submitButtonXpath);	// second page click on proper truck size
			driver.click(bypassButtonXpath);
			driver.click(secondBypassXpath);
			
			
			List<WebElement> results = driver.getElements(xpaths);
			
			return results.stream().map(result -> mapToTruck(result, websiteName)).collect(Collectors.toList());
		}
		
		finally {
			driver.tearDown();
		}
	}
	
	public List<Truck> getEnterpriseResults(String zipcode) throws Exception {
		String websiteName = "Enterprise";
		String websiteUrl = "http://uhaul.com/";
		
		List<String> xpaths = new ArrayList<String>();
		
		try {
			driver.navigateTo(websiteUrl);
			driver.click("");	// xpath is parameter
			driver.click("");	// second page click on proper truck size
			
			List<WebElement> results = driver.getElements(xpaths);
			
			return results.stream().map(result -> mapToTruck(result, websiteName)).collect(Collectors.toList());
		}
		
		finally {
			driver.tearDown();
		}
	}
	
	public List<Truck> getBudgetResults(String zipcode) throws Exception {
		String websiteName = "Budget";
		String websiteUrl = "http://uhaul.com/";
		
		List<String> xpaths = new ArrayList<String>();
		
		try {
			driver.navigateTo(websiteUrl);
			driver.click("");	// xpath is parameter
			driver.click("");	// second page click on proper truck size
			
			List<WebElement> results = driver.getElements(xpaths);
			
			return results.stream().map(result -> mapToTruck(result, websiteName)).collect(Collectors.toList());
		}
		
		finally {
			driver.tearDown();
		}
	}
	
	public static WebScraper instanceOf() {
		return new WebScraper();
	}
}

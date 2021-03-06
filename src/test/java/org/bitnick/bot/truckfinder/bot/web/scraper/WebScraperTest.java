package org.bitnick.bot.truckfinder.bot.web.scraper;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.bitnick.bot.truckfinder.database.model.Truck;
import org.junit.Ignore;
import org.junit.Test;

public class WebScraperTest {
	private final WebScraper webScraperTest = WebScraper.instanceOf();
	
	@Ignore
	@Test
	public void returnsNotNullForGetUhaulResults() throws Exception {
		List<Truck> results = webScraperTest.getUhaulResults("newport, mn", "07/31/2018", "5:30 am", "9\'");
		
		results.forEach(result -> System.out.println(result.getPrice()));
		
		assertNotNull(results);
	}
	
	@Ignore
	@Test
	public void returnsNotNullForPenskeResults() throws Exception {
		List<Truck> results = webScraperTest.getPenskeResults("newport, mn", "07/30/2018", "5:30 am", "9\'");
		
		results.forEach(result -> System.out.println(result.getPrice()));
		
		assertNotNull(results);
	}
	
	@Ignore
	@Test
	public void returnsNotNullForBudgetTruck() throws Exception {
		List<Truck> results = webScraperTest.getBudgetResults("newport, mn", "2018-7-30", "6:00 AM", "12\'");
		
		results.forEach(result -> System.out.println(result.getPrice()));
		
		assertNotNull(results);
	}

	@Test
	public void returnsNotNullForAllWebSites() throws Exception {
		List<Truck> results = webScraperTest.getAllResults("55016", "07/30/2018", "6:00 AM", "12");
		
		results.forEach(result -> System.out.println(result.getPrice()));
		
		assertNotNull(results);
	}
}

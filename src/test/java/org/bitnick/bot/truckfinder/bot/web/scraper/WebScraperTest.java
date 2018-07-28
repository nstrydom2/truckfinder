package org.bitnick.bot.truckfinder.bot.web.scraper;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.bitnick.bot.truckfinder.database.model.Truck;
import org.junit.Test;

public class WebScraperTest {
	private final WebScraper webScraperTest = WebScraper.instanceOf();
	
	@Test
	public void returnsNotNullForGetUhaulResults() throws Exception {
		List<Truck> results = webScraperTest.getUhaulResults("newport, mn", "07/25/2018", "5:30 am", "9\'");
		
		results.forEach(result -> System.out.println(result.getPrice()));
		
		assertNotNull(results);
	}
	
	@Test
	public void returnsNotNullForPenskeResults() throws Exception {
		List<Truck> results = webScraperTest.getPenskeResults("newport, mn", "07/25/2018", "5:30 am", "9\'");
		
		results.forEach(result -> System.out.println(result.getPrice()));
		
		assertNotNull(results);
	}
}

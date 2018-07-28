package org.bitnick.bot.truckfinder.bot.web.scraper;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class DriverTest {
	private final Driver driverTest = Driver.instanceOf();
	
	@Test
	public void returnsResponseBasedOnSuccessfulNavigation() throws Exception {
		try {
			driverTest.navigateTo("https://google.com/");
			
			String urlTest = driverTest.driver.getCurrentUrl();
			
			System.out.println(urlTest);
			assertNotNull(urlTest);
		}
		
		finally {
			driverTest.tearDown();
		}
	}
}

package org.bitnick.bot.truckfinder.web.controller;

import java.util.List;

import org.bitnick.bot.truckfinder.bot.web.scraper.WebScraper;
import org.bitnick.bot.truckfinder.database.model.Truck;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Component
@Controller
public class BotRestController {
	private WebScraper webScraper;
	
	@Autowired
	public void setWebScraper(WebScraper webScraper) {
		this.webScraper = WebScraper.instanceOf();
	}
	
	@RequestMapping(value = "/getAllTrucks/{zipcode}")
	public List<Truck> getAllTrucks(@PathVariable("zipcode") String zipcode) throws Exception {
		return webScraper.getUhaulResults(zipcode);
	}
}

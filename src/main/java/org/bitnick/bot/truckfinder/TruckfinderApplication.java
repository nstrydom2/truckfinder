package org.bitnick.bot.truckfinder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan
public class TruckfinderApplication {

	public static void main(String[] args) {
		SpringApplication.run(TruckfinderApplication.class, args);
	}
}

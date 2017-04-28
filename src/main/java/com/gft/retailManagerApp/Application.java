package com.gft.retailManagerApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;

/**
 * Entry point of the application
 *
 * @author Vanessa Cantalapiedra
 * @version 1.0
 * @since 20-04-2017
 */

@SpringBootApplication
public class Application extends AsyncConfigurerSupport {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}

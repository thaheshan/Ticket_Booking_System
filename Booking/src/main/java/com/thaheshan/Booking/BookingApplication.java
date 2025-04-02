package com.thaheshan.Booking;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;


import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.autoconfigure.domain.EntityScan;





@SpringBootApplication
@EntityScan(basePackages = "com.thaheshan.Booking")
public class BookingApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookingApplication.class, args);
	}

	// Use CommandLineRunner to integrate your ticket system logic
	@org.springframework.context.annotation.Bean
	CommandLineRunner commandLineRunner() {
		return args -> {
			Booking ticketSystemMenu = new Booking();
			ticketSystemMenu.runMenu();
		};
	}
}

package com.github.jezstewartdev.customerapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.github.jezstewartdev.customerapp.service.CustomerService;

@SpringBootApplication
public class CustomerAppApplication implements CommandLineRunner {

	@Autowired
	CustomerService customerService;

	public static void main(String[] args) {
		SpringApplication.run(CustomerAppApplication.class, args);
	}

	@Override
	// use run from commandlinerunner to run code just after application starts
	public void run(String... args) throws Exception {
		if (args.length > 0) {
			customerService.loadCustomers(args[0]);
		}
	}

}

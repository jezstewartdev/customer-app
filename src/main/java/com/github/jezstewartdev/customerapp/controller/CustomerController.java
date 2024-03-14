package com.github.jezstewartdev.customerapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.jezstewartdev.customerapp.domain.CustomerDto;
import com.github.jezstewartdev.customerapp.service.CustomerService;

import jakarta.validation.Valid;

@RequestMapping("/customer")
@RestController
public class CustomerController {
	
	@Autowired
	CustomerService customerService;
	
	@GetMapping("/{id}")
	public CustomerDto getCustomer(@PathVariable("id") int id) {
		return customerService.getCustomerById((id));
	}
	
	@PostMapping
	public void addCustomer(@Valid @RequestBody CustomerDto customer) {
		customerService.addCustomer(customer);
	}
	

}

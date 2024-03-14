package com.github.jezstewartdev.customerapp.service;

import org.hibernate.ObjectNotFoundException;

import com.github.jezstewartdev.customerapp.domain.CustomerDto;


public interface CustomerService {
	
	public CustomerDto getCustomerById(int id) throws ObjectNotFoundException;
	
	public void addCustomer(CustomerDto customer);
	
	public void loadCustomers(String path);

}

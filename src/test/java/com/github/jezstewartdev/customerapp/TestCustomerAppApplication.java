package com.github.jezstewartdev.customerapp;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.github.jezstewartdev.customerapp.service.CustomerService;


@ExtendWith(MockitoExtension.class)
class TestCustomerAppApplication {

    @Mock
    private CustomerService service;

    @InjectMocks
    private CustomerAppApplication application;

	@Test
	void shouldCallLoadCustomersWhenArgsExist() throws Exception {
		
		String path = "somePath";
		String[] args = {path};
		application.run(args);
        verify(service, times(1)).loadCustomers(path);		
	
	}
	
	@Test
	void shouldNotCallLoadCustomerwWhenAgsDoNotExist() throws Exception {
		
		String path = "somePath";
		application.run();
        verify(service, times(0)).loadCustomers(path);	

	
	}

}

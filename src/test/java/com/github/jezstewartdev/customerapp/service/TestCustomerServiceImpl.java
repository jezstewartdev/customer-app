package com.github.jezstewartdev.customerapp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.hibernate.ObjectNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

import com.github.jezstewartdev.customerapp.domain.CustomerDto;
import com.github.jezstewartdev.customerapp.entity.Customer;
import com.github.jezstewartdev.customerapp.repository.CustomerRepository;

@ExtendWith(MockitoExtension.class)
class TestCustomerServiceImpl {
	
	@Mock
	CustomerRepository customerRepository;
	
	@Mock
	Customer customer;
	
	@Mock
	CustomerDto customerDto;
	
	@Mock
	RestTemplate restTemplate;
	
	@Mock
	ModelMapper mapper;
	
	@Spy
	@InjectMocks
	CustomerServiceImpl customerServiceImpl;

	@TempDir
	File tempDir;
	
	@Value("${api.customer.add.url}")
    private String addUrl;
	
	ArgumentCaptor<CustomerDto> customerDtoArgument = ArgumentCaptor.forClass(CustomerDto.class);

	@Test
	void getCustomerByIdShouldReturnCustomerDto() throws Exception {
		
		when(customerRepository.findById(anyInt())).thenReturn(Optional.of(customer));
		when(mapper.map(customer, CustomerDto.class)).thenReturn(customerDto);
		
		assertEquals(customerServiceImpl.getCustomerById(anyInt()),customerDto);
	}
	
	@Test
	void getCustomerByIdShouldThrowObjectNotFoundException() throws Exception {
		
		when(customerRepository.findById(anyInt())).thenReturn(Optional.empty());
		
		assertThrows(ObjectNotFoundException.class, () -> customerServiceImpl.getCustomerById(anyInt()));
	
	}
		
	@Test
	void addCustomerShouldSaveCustomerToRepository() throws Exception {
		
		when(mapper.map(customerDto, Customer.class)).thenReturn(customer);
		
		customerServiceImpl.addCustomer(customerDto);
			
		verify(customerRepository).save(customer);
	
	}
	
	@Test
	void loadCustomersShouldSendCorrectCustomerDtoObjectToPostApi() throws Exception {
		
		File csvFile = mockCsvFile();   
		doReturn(new FileReader(csvFile)).when(customerServiceImpl).getFileReader(anyString());
		
		customerServiceImpl.loadCustomers(anyString());
		
		verify(restTemplate).postForEntity(eq(addUrl), customerDtoArgument.capture(), eq(CustomerDto.class));
		assertEquals(expectedCustomerDto(), customerDtoArgument.getValue());
		
	}

	private File mockCsvFile() throws IOException {
		File csvFile = new File(tempDir, "csv.txt");
		List<String> lines = Arrays.asList("1,Michael Scott,1725 Slough Avenue,Scranton Business Park,Pennsylvania,Lackawanna,USA,18505,2,Michael Scott,1725 Slough Avenue,Scranton Business Park,Pennsylvania,Lackawanna,USA,18505");
	    Files.write(csvFile.toPath(), lines);
		return csvFile;
	}
	
	private CustomerDto expectedCustomerDto() {
		CustomerDto customerDto = new CustomerDto();
		customerDto.setCustomerRef(1); 
		customerDto.setName("Michael Scott"); 
		customerDto.setAddressLine1("1725 Slough Avenue");
		customerDto.setAddressLine2("Scranton Business Park"); 
		customerDto.setTown("Pennsylvania"); 
		customerDto.setCounty("Lackawanna"); 
		customerDto.setCountry("USA");
		customerDto.setPostcode("18505"); 
		return customerDto;
	}

}

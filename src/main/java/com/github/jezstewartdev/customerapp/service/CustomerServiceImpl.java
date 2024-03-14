package com.github.jezstewartdev.customerapp.service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.hibernate.ObjectNotFoundException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.github.jezstewartdev.customerapp.domain.CustomerDto;
import com.github.jezstewartdev.customerapp.entity.Customer;
import com.github.jezstewartdev.customerapp.repository.CustomerRepository;

@Component
public class CustomerServiceImpl implements CustomerService {

	private static final String CUSTOMER_REF = "Customer Ref";
	private static final String CUSTOMER_NAME = "Customer Name";
	private static final String ADDRESS_LINE_1 = "Address Line 1";
	private static final String ADDRESS_LINE_2 = "Address Line 2";
	private static final String TOWN = "Town";
	private static final String COUNTY = "County";
	private static final String COUNTRY = "Country";
	private static final String POSTCODE = "Postcode";
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerServiceImpl.class);

	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private ModelMapper mapper;
	
	@Value("${api.customer.add.url}")
    private String addUrl;
	
	@Override
	public CustomerDto getCustomerById(int id) {
		return customerRepository.findById(id).map(customer -> mapper.map(customer, CustomerDto.class)).orElseThrow(() -> new ObjectNotFoundException(new Customer(), String.valueOf(id)));
	}

	@Override
	public void addCustomer(CustomerDto customer) {
		customerRepository.save(mapper.map(customer, Customer.class));
	}

	@Override
	public void loadCustomers(String path) {
		List<CustomerDto> customers = createCustomerListFromFile(path);
		sendAddRequests(customers);
	}

	private List<CustomerDto> createCustomerListFromFile(String path) {
		List<CustomerDto> customers = new ArrayList<>();
		try {
			Reader in = getFileReader(path);
			BufferedReader reader = new BufferedReader(in); 
			Iterable<CSVRecord> records = createCSVRecords(reader);
			for (CSVRecord record : records) {
				CustomerDto customer = createCustomerDto(record);
				customers.add(customer);
			}
		} catch (FileNotFoundException e) {
			LOGGER.warn("Customers file not found.  No customers were loaded");
		} catch (IOException | IllegalArgumentException e) {
			LOGGER.warn("Invalid customers file.  No customers were loaded");
		} 
		return customers;
	}

	protected Reader getFileReader(String path) throws FileNotFoundException {
		Reader in = new FileReader(path);
		return in;
	}
	
	private void sendAddRequests(List<CustomerDto> customers) {
		for (CustomerDto customer : customers) {
			restTemplate.postForEntity(addUrl, customer, CustomerDto.class);
		}
	}

	private Iterable<CSVRecord> createCSVRecords(Reader in) throws IOException {
		Iterable<CSVRecord> records;
		records = CSVFormat.RFC4180.builder()
				.setHeader(CUSTOMER_REF, CUSTOMER_NAME, ADDRESS_LINE_1, ADDRESS_LINE_2, TOWN, COUNTY, COUNTRY, POSTCODE)
				.build().parse(in);
		return records;
	}

	private CustomerDto createCustomerDto(CSVRecord record) {
		CustomerDto customer = new CustomerDto();
		customer.setCustomerRef(Integer.valueOf(record.get(CUSTOMER_REF)));
		customer.setName(record.get(CUSTOMER_NAME));
		customer.setAddressLine1(record.get(ADDRESS_LINE_1));
		customer.setAddressLine2(record.get(ADDRESS_LINE_2));
		customer.setTown(record.get(TOWN));
		customer.setCounty(record.get(COUNTY));
		customer.setCountry(record.get(COUNTRY));
		customer.setPostcode(record.get(POSTCODE));
		return customer;
	}

}

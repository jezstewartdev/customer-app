package com.github.jezstewartdev.customerapp.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.github.jezstewartdev.customerapp.domain.CustomerDto;
import com.github.jezstewartdev.customerapp.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
class TestCustomerController
{
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @InjectMocks
    private CustomerController controller;

    @Mock
    private CustomerService service;

    @Mock
    private CustomerDto customer;

    @BeforeEach
    public void setup()
    {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void getCustomerShouldReturnCustomer() throws Exception
    {
        when(service.getCustomerById(anyInt())).thenReturn(customer);

        mockMvc.perform(get("/customer/{id}", anyInt())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(customer)));
    }
    
    @Test
    void addCustomerShouldPassCustomerDtoToCustomerService() throws Exception
    {
        CustomerDto newCustomer = new CustomerDto();
        newCustomer.setCustomerRef(0);
        newCustomer.setName("Jeff Bezos");
        
        mockMvc.perform(post("/customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCustomer)))
                .andExpect(status().isOk());

        verify(service).addCustomer(newCustomer);
        
    }

   @Test
    void addCustomerShouldNotCallCustomerServiceIfNameOmitted() throws Exception
    {
        CustomerDto newCustomer = new CustomerDto();
        newCustomer.setCustomerRef(0);

        mockMvc.perform(post("/customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCustomer)))
                .andExpect(status().isBadRequest());

        verify(service, never()).addCustomer(any(CustomerDto.class));
    }

}

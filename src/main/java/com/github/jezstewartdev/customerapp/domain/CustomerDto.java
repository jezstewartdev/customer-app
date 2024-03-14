package com.github.jezstewartdev.customerapp.domain;

import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Validated
public class CustomerDto {
	
	private int customerRef; 
	@NotNull(message="Name must be entered")
	private String name;
	private String addressLine1;
	private String addressLine2;
	private String town;
	private String county;
	private String country;
	private String postcode;
	
}

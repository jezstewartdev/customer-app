package com.github.jezstewartdev.customerapp.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Entity //jpa annotation - h2 table created automatically based on this
@AllArgsConstructor //should we be exluding id??
@NoArgsConstructor
@Getter
@Setter
public class Customer {
	
	@Id
	private int customerRef;
	@NonNull
	private String name;
	private String addressLine1;
	private String addressLine2;
	private String town;
	private String county;
	private String country;
	private String postcode;

}

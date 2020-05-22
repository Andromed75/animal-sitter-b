package com.example.animalsitter.domain;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Address {
	
	@Id
	@GeneratedValue
	UUID id;
	
	String streetNumber;
	
	String street;
	
	Double latitude;
	
	Double longitude;
	
	String postalcode;
	
	String city;
	
	String country;
	
	
}

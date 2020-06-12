package com.example.animalsitter.dto;

import java.time.OffsetDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SearchSittingDto {

	
	OffsetDateTime beg;
	
	OffsetDateTime end;
	
	String postcode;
	
}

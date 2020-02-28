package com.example.animalsitter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OffsetDateDTO {

	int year;
	int month;
	int dayOfMonth;
	String local;
}

package com.example.animalsitter.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import com.example.animalsitter.domain.Animal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SittingDto {

	String title;

	List<Animal> animals;

	UUID userId;

	String shiftBeggining;

	String shiftEnd;
	
	LocalDate createdDate;

}

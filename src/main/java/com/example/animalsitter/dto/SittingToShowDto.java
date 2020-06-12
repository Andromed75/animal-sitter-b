package com.example.animalsitter.dto;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

import com.example.animalsitter.domain.Animal;
import com.example.animalsitter.domain.Sitting;
import com.example.animalsitter.domain.User;
import com.example.animalsitter.exception.AnimalNotFoundException;
import com.example.animalsitter.repository.Animalrepository;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SittingToShowDto {

	String title;

	String description;

	Animal animal;

	OffsetDateTime shiftBeggining;

	OffsetDateTime shiftEnd;

	String firstName;

	String lastName;

	int age;

	String phone;

	String email;
	
	// double rating;
	
	String city;
	
	String street;
	
	Double lon;
	
	Double lat;

}

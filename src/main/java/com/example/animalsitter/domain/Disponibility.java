package com.example.animalsitter.domain;

import java.time.LocalDate;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Disponibility {

	@Id
	@GeneratedValue
	UUID id;
	
	LocalDate shiftBeggining;
	
	LocalDate shiftEnd;
	
	boolean available;
	
	
}

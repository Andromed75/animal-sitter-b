package com.example.animalsitter.domain;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Sitting {

	@Id
	@GeneratedValue
	UUID id;
	
	String title;
	
	String description;
	
	// UUID animalId;
	
//	@Transient
	@OneToOne(targetEntity = Animal.class)
	Animal animal;
	
	UUID userId;
	
	@ManyToMany(targetEntity = Status.class, cascade = CascadeType.ALL)
	List<Status> status;
	
	OffsetDateTime shiftBeggining;

	OffsetDateTime shiftEnd;
	
	LocalDate createdDate;	
	
}

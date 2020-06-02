package com.example.animalsitter.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

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
	
	@OneToMany(targetEntity = Animal.class, cascade = CascadeType.ALL)
	List<Animal> animals;
	
	@OneToOne(targetEntity = User.class, cascade = CascadeType.ALL)
	User user;
	
	@ManyToOne(targetEntity = Status.class, cascade = CascadeType.ALL)
	Status status;
	
	OffsetDateTime shiftBeggining;

	OffsetDateTime shiftEnd;
	
	LocalDate createdDate;	
	
}

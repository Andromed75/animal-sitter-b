package com.example.animalsitter.domain;

import java.util.List;
import java.util.UUID;

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
	
	@OneToMany(targetEntity = Animal.class)
	List<Animal> animals;
	
	@OneToOne(targetEntity = User.class)
	User user;
	
	@ManyToOne(targetEntity = Status.class)
	Status status;
	
	@OneToOne(targetEntity = Disponibility.class)
	Disponibility disponibility;
	
}

package com.example.animalsitter.domain;

import java.util.List;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class User extends Admin{

	@OneToMany(targetEntity = Animal.class)
	List<Animal> animals;
	
	@OneToMany(targetEntity = Disponibility.class)
	List<Disponibility> disponibility;
	
	public User(UUID id, String pseudo, String password, List<Animal> animals) {
		super(id, pseudo, password);
		this.animals = animals;
	}
	
	

}

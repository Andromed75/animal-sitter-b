package com.example.animalsitter.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import com.example.animalsitter.dto.UserDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User extends Admin{
	
	
	
	@OneToMany(targetEntity = Animal.class, cascade = CascadeType.ALL)
	List<Animal> animals;
	
	@OneToMany(targetEntity = Disponibility.class, cascade = CascadeType.ALL)
	List<Disponibility> disponibility;
	

	
	public User(UUID id, String pseudo, String password, String email, List<Animal> animals, List<Disponibility> disponibility, Set<Role> roles ) {
		super(id, pseudo, password, email, roles);
		this.animals = animals;
		this.disponibility = disponibility;
	}
	
}

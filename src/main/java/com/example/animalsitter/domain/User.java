package com.example.animalsitter.domain;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This is the User entity matching a User's profile, it extends from admin with pseudo password email and role
 * 
 * @author ae.de-donno
 *
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends Admin{
	
	String firstName;
	
	String lastName;
	
	int age;
	
	String phone;
	
	@OneToOne(targetEntity = Address.class, cascade = CascadeType.ALL)
	Address adress;
	
	@OneToMany(targetEntity = Animal.class, cascade = CascadeType.ALL)
	List<Animal> animals;
	
	@OneToMany(targetEntity = Sitting.class, cascade = CascadeType.ALL)
	List<Sitting> sittings;
	
	@ElementCollection(targetClass = Integer.class)
	List<Integer> rating;
	

	
	public User(UUID id, String pseudo, String password, String email, List<Animal> animals, List<Sitting> sittings, Set<Role> roles, Address address, List<Integer> rating) {
		super(id, pseudo, password, email, roles);
		this.animals = animals;
		this.sittings = sittings;
		this.adress = address;
		this.rating = rating;
	}
	
	/**
	 * @return the average rating of a user
	 */
	public double getUserRating(){
		int sum = 0;
		double result = 0;
		if(rating.size() > 0) {
			for(Integer rating : rating) {
				sum =+ rating;
			}
			result = sum / rating.size();
		}
		return  result;
	}
}

package com.example.animalsitter.dto;

import java.util.List;
import java.util.UUID;

import com.example.animalsitter.domain.Address;
import com.example.animalsitter.domain.Animal;
import com.example.animalsitter.domain.Disponibility;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoFullDto {
	
UUID id;
	
	String pseudo;
		
	String email;

	String firstName;

	String lastName;

	int age;

	String phone;

	Address adress;

	List<Animal> animals;

}

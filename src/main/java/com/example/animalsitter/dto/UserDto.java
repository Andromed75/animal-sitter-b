package com.example.animalsitter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDto {

	String pseudo;
	
	String password;
	
	String email;
	
}

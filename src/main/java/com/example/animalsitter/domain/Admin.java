package com.example.animalsitter.domain;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Admin {

	
	@Id
	@GeneratedValue
	UUID id;
	
	String pseudo;
	
	String password;
	
}

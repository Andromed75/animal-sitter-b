package com.example.animalsitter.domain;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Sickness {

	@Id
	@GeneratedValue
	UUID id;
	
	String name;
	
	String description;
	
	String importance;
	
}

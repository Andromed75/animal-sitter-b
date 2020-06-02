package com.example.animalsitter.domain;

import java.time.LocalDate;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Comment {

	@Id
	@GeneratedValue
	UUID id;
	
	@OneToOne(targetEntity = User.class, cascade = CascadeType.ALL)
	User user;
	
	String commentaire;
	
	LocalDate commentDate;
	
}

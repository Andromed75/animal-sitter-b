package com.example.animalsitter.domain;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Photo {

	@Id
	@GeneratedValue
	UUID id;
	
	String name;
	
	String type;
		
	@Column(name = "image", length = 1000)
    private byte[] image;
	
}

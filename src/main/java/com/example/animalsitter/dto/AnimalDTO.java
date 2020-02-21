package com.example.animalsitter.dto;

import java.util.UUID;

import com.example.animalsitter.domain.Animal;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class AnimalDTO {

	UUID id;
	
	String name;
	
	String tatoo;
	
	Integer age;
	
	String furColor;
	
	String eyesColor;
	
	Boolean sick;
	
	@JsonProperty(value = "when_do_i_eat")
    String whenDoIEat;

	@JsonProperty(value = "what_do_i_eat")
	String whatDoIEat;
	
	public static AnimalDTO of(Animal animal) {
		return AnimalDTO.builder()
		.id(animal.getId())
		.name(animal.getName())
		.tatoo(animal.getTatoo())
		.age(animal.getAge())
		.furColor(animal.getFurColor())
		.eyesColor(animal.getEyesColor())
		.sick(animal.getSick())
		.whenDoIEat(animal.getWhenDoIEat())
		.whatDoIEat(animal.getWhatDoIEat())
		.build();
	}
	
}

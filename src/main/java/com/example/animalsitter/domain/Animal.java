package com.example.animalsitter.domain;

import java.util.List;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.example.animalsitter.dto.request.AnimalWithUserId;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "animal")
public class Animal {

	@Id
	@GeneratedValue
	UUID id;
	
	String name;
	
	String tatoo;
	
	Integer age;
	
    String furColor;
	
	String eyesColor;
	
	Boolean sick;
	
	String whenDoIEat;
	
	String whatDoIEat;
	
	@OneToMany(targetEntity = Sickness.class)
	List<Sickness> sicknesses;
//	
//	List<Allergy> allergies;
//	
//	List<Treatment> treatments;

	public static Animal of(AnimalWithUserId dto) {
		Animal animal = new Animal();
		animal.setName(dto.getName());
		animal.setTatoo(dto.getTatoo());
		animal.setAge(dto.getAge());
		animal.setFurColor(dto.getFurColor());
		animal.setEyesColor(dto.getEyesColor());
		animal.setSick(dto.getSick());
		animal.setWhatDoIEat(dto.getWhatDoIEat());
		animal.setWhenDoIEat(dto.getWhenDoIEat());
		return animal;
		
	}
}

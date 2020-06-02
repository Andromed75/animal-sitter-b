package com.example.animalsitter.domain;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.example.animalsitter.dto.request.AnimalWithUserId;

import javassist.bytecode.ByteArray;
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
	
	@OneToOne(targetEntity = Photo.class, cascade = CascadeType.ALL)
	Photo photo;
	
	@OneToMany(targetEntity = Sickness.class, cascade = CascadeType.ALL)
	List<Sickness> sicknesses;
//	
//	List<Allergy> allergies;
//	
//	List<Treatment> treatments;

	public static Animal of(AnimalWithUserId dto){
		Animal animal = new Animal();
		animal.setName(dto.getName());
		animal.setTatoo(dto.getTatoo());
		animal.setAge(dto.getAge());
		animal.setFurColor(dto.getFurColor());
		animal.setEyesColor(dto.getEyesColor());
		animal.setSick(dto.getSick());
		animal.setWhatDoIEat(dto.getWhatDoIEat());
		animal.setWhenDoIEat(dto.getWhenDoIEat());
		Photo photo = Photo.builder().build();
		animal.setPhoto(photo);
		return animal;
	}
}

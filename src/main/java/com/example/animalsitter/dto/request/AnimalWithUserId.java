package com.example.animalsitter.dto.request;

import java.util.UUID;

import javax.persistence.Lob;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class AnimalWithUserId {

	UUID userId;

	String name;

	String tatoo;

	Integer age;

	String furColor;

	String eyesColor;

	Boolean sick;

	String whenDoIEat;

	String whatDoIEat;
	
	@Lob
	MultipartFile photo;

}

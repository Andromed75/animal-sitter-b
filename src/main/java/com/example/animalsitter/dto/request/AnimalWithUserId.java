package com.example.animalsitter.dto.request;

import java.util.UUID;

import com.example.animalsitter.domain.Animal;

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

}

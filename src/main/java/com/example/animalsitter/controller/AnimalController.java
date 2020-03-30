package com.example.animalsitter.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.animalsitter.domain.Animal;
import com.example.animalsitter.domain.Sickness;
import com.example.animalsitter.dto.AnimalDTO;
import com.example.animalsitter.repository.Animalrepository;

import lombok.extern.slf4j.Slf4j;

@CrossOrigin
@RestController
@RequestMapping("api/animal/v1")
@Slf4j 
public class AnimalController { 

	@Autowired
	Animalrepository animalRepo;
	
	@GetMapping("/findall")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<List<Animal>> getAllAnimals() {
		return ResponseEntity.ok(animalRepo.findAll());
	}
	
	@PostMapping("/create")
	public ResponseEntity<Animal> createAnimal(@RequestBody AnimalDTO animalDTO) {
		log.info("HTTP Handling createAnimal");
		Animal animal = Animal.builder().name(animalDTO.getName()).sicknesses(new ArrayList<Sickness>()).build();
		return ResponseEntity.ok(animalRepo.save(animal));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Animal> getAnimalById(@PathVariable("id") UUID id) {
		log.info("HTTP Handling : getAnimalById, id={}", id);
		return ResponseEntity.ok(animalRepo.getOne(id));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteAnimalById(@PathVariable("id") UUID id) {
		log.info("HTTP Handling deleteAnimalById, id={}", id);
		animalRepo.deleteById(id);
		return ResponseEntity.ok().build();
	}
	
	
	
}

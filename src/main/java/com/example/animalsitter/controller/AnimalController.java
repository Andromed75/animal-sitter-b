package com.example.animalsitter.controller;

import java.io.IOException;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.animalsitter.domain.Animal;
import com.example.animalsitter.dto.AnimalDTO;
import com.example.animalsitter.repository.Animalrepository;
import com.example.animalsitter.service.AnimalService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author ae.de-donno
 *
 */
@CrossOrigin
@RestController
@RequestMapping("api/animal/v1")
@Slf4j
public class AnimalController {

	@Autowired
	Animalrepository animalRepo;

	@Autowired
	AnimalService animalService;

	@GetMapping("/findall")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<List<Animal>> getAllAnimals() {
		log.info("Http Handling getAllAnimals");
		return ResponseEntity.ok(animalService.findAll());
	}

	@PostMapping("/create")
	public ResponseEntity<Animal> createAnimal(@RequestBody AnimalDTO animalDTO) {
		log.info("HTTP Handling createAnimal");
		log.info("PHOTO : {}", animalDTO.getPhoto());
		try {
			return ResponseEntity.ok(animalService.createAnimal(animalDTO, animalDTO.getPhoto()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.info("Error creating animal : {}", e.getMessage());
		}
		return ResponseEntity.badRequest().build();
	}

	@GetMapping("/{id}")
	public ResponseEntity<Animal> getAnimalById(@PathVariable("id") UUID id) {
		log.info("HTTP Handling : getAnimalById, id={}", id);
		return ResponseEntity.ok(animalService.getOne(id));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteAnimalById(@PathVariable("id") UUID id) {
		log.info("HTTP Handling deleteAnimalById, id={}", id);
		animalService.deleteAnimal(id);
		return ResponseEntity.ok().build();
	}
	
	@PutMapping
	public ResponseEntity<Animal> updateANimal(@RequestBody Animal animal){
		log.info("HTTP Handling updateANimal, id={}", animal.getId());
		return ResponseEntity.ok(animalService.updateAnimal(animal));
	}

}

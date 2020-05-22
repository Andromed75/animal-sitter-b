package com.example.animalsitter.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.animalsitter.domain.Animal;
import com.example.animalsitter.domain.Sickness;
import com.example.animalsitter.dto.AnimalDTO;
import com.example.animalsitter.repository.Animalrepository;

@Service
public class AnimalService {

	@Autowired
	Animalrepository animalRepository;

	/**
	 * @return all animals in database
	 */
	public List<Animal> findAll() {
		return animalRepository.findAll();
	}

	/**
	 * @param animalDTO
	 * @return the animal created
	 */
	public Animal createAnimal(AnimalDTO animalDTO) {
		Animal animal = Animal.builder().name(animalDTO.getName()).sicknesses(new ArrayList<Sickness>()).build();
		return animalRepository.save(animal);
	}
	
	/**
	 * @param id
	 * @return the animal with given ID
	 */
	public Animal getOne(UUID id) {
		return animalRepository.getOne(id);
	}
	
	/**
	 * @param id
	 * delete the animal with given ID
	 */
	public void deleteAnimal(UUID id) {
		animalRepository.deleteById(id);
	}
}

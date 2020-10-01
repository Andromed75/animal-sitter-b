package com.example.animalsitter.service;

import java.io.IOException;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.animalsitter.domain.Animal;
import com.example.animalsitter.domain.Photo;
import com.example.animalsitter.domain.Sickness;
import com.example.animalsitter.dto.AnimalDTO;
import com.example.animalsitter.exception.AnimalNotFoundException;
import com.example.animalsitter.repository.Animalrepository;

/**
 * @author ae.de-donno
 *
 * Service associated to {@link Animal}}
 *
 */
@Service
public class AnimalService {

	Animalrepository animalRepository;
	
	@Autowired
	public AnimalService(Animalrepository animalRepository) {
		this.animalRepository = animalRepository;
	}

	/**
	 * @return all animals in database
	 */
	public List<Animal> findAll() {
		return animalRepository.findAll();
	}

	/**
	 * @param animalDTO
	 * @param multipartFile 
	 * @return the animal created
	 * @throws IOException 
	 */
	public Animal createAnimal(AnimalDTO animalDTO, MultipartFile file) throws IOException {
		byte[] byteObjects = null;
		if(file != null) {
			byteObjects = new byte[file.getBytes().length];
			int i= 0;
			for (byte b : file.getBytes()){
				byteObjects[i++] = b;
			}			
		}
		
		Photo photo = Photo.builder().image(byteObjects).build();

		Animal animal = Animal.builder().name(animalDTO.getName()).photo(photo).sicknesses(new ArrayList<Sickness>()).build();
		
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

	public Animal updateAnimal(Animal fromApp) {
		Optional<Animal> fromBase = animalRepository.findById(fromApp.getId());
		if(!fromBase.isPresent()) {
			throw new AnimalNotFoundException("Animal not found");
		}
		Animal updated = fromBase.get();
		updated.setAge(fromApp.getAge());
		updated.setEyesColor(fromApp.getEyesColor());
		updated.setFurColor(fromApp.getFurColor());
		updated.setName(fromApp.getName());
		updated.setSick(fromApp.getSick());
		updated.setSpecies(fromApp.getSpecies());
		updated.setTatoo(fromApp.getTatoo());
		updated.setWhatDoIEat(fromApp.getWhatDoIEat());
		updated.setWhenDoIEat(fromApp.getWhenDoIEat());
		return animalRepository.save(updated);
	}
}

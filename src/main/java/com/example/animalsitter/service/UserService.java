package com.example.animalsitter.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.animalsitter.domain.Animal;
import com.example.animalsitter.domain.User;
import com.example.animalsitter.dto.request.AnimalWithUserId;
import com.example.animalsitter.exception.UserNotFoundException;
import com.example.animalsitter.repository.Animalrepository;
import com.example.animalsitter.repository.UserRepository;

import javassist.tools.reflect.CannotCreateException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserService {
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	Animalrepository animalRepository;

	public List<Animal> getUsersAnimals(UUID id) {
		Optional<User> user = userRepository.findById(id);
		List<Animal> userAnimals = new ArrayList<Animal>();
		if(user.isPresent()) {
			userAnimals = user.get().getAnimals();
		}
		return userAnimals;
	}

	public User addAnimalToUser(AnimalWithUserId dto) throws CannotCreateException {
		Optional<User> optionalUser = userRepository.findById(dto.getUserId());
		if (!optionalUser.isPresent()) {
			log.info("User with id = {} not found", dto.getUserId());
			throw new UserNotFoundException("User not found");
		}
		User user = optionalUser.get();
		checkHowManyAnimalsUsersGot(user);
		Animal animal = Animal.of(dto);
		animalRepository.save(animal);
		user.getAnimals().add(animal);
		return userRepository.save(user);
	}

	public void checkHowManyAnimalsUsersGot(User user) throws CannotCreateException {
		List<Animal> usersAnimals = user.getAnimals();
		if(usersAnimals != null && usersAnimals.size() > 2) {
			throw new CannotCreateException("User already have three animals created");
		}
	}
}

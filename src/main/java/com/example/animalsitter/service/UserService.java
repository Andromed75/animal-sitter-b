package com.example.animalsitter.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.animalsitter.domain.Animal;
import com.example.animalsitter.domain.User;
import com.example.animalsitter.dto.UserInfoFullDto;
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

	public User updateUser(UserInfoFullDto uifdto) {
		Optional<User> userFromBaseOptional = userRepository.findById(uifdto.getId());
		if(!userFromBaseOptional.isPresent()) {
			log.info("User with id = {} not found", uifdto.getId());
			throw new UserNotFoundException("User not found");
		}
		User fromBase = userFromBaseOptional.get();
		if(fromBase.getEmail().equals(uifdto.getEmail())) {
			// send mail
		}
		fromBase.setAge(uifdto.getAge());
		fromBase.setEmail(uifdto.getEmail());
		fromBase.setFirstName(uifdto.getFirstName());
		fromBase.setLastName(uifdto.getLastName());
		fromBase.setPhone(uifdto.getPhone());
		// Test voir s'il existe
		fromBase.setPseudo(uifdto.getPseudo());
		fromBase.getAdress().setCity(uifdto.getAdress().getCity());
		fromBase.getAdress().setCountry(uifdto.getAdress().getCountry());
		fromBase.getAdress().setPostalcode(uifdto.getAdress().getPostalcode());
		fromBase.getAdress().setStreet(uifdto.getAdress().getStreet());
		
		return userRepository.save(fromBase);
	}

	public boolean checkIfUserHasAnimals(UUID id) {
		Optional<User> optionalUser = userRepository.findById(id);
		if(!optionalUser.isPresent()) {
			throw new UserNotFoundException("User not found");
		}
		User user = optionalUser.get();
		boolean response = false;
		if(user.getAnimals() != null && user.getAnimals().size() > 0 && !user.getAnimals().isEmpty()) {
			response = true;
		}
		return response;
	}
}

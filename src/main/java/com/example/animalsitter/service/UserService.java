package com.example.animalsitter.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.animalsitter.controller.UserController;
import com.example.animalsitter.domain.Animal;
import com.example.animalsitter.domain.User;
import com.example.animalsitter.dto.UserInfoFullDto;
import com.example.animalsitter.dto.request.AnimalWithUserId;
import com.example.animalsitter.exception.UserNotFoundException;
import com.example.animalsitter.repository.Animalrepository;
import com.example.animalsitter.repository.UserRepository;

import javassist.tools.reflect.CannotCreateException;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ae.de-donno 
 * 
 * @apiNote Service linked to the {@link UserController}
 *
 */
@Service
@Slf4j
public class UserService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	Animalrepository animalRepository;

	/**
	 * Retrives all {@link Animal} from {@link User}
	 * 
	 * @param id
	 * @return
	 */
	public List<Animal> getUsersAnimals(UUID id) {
		Optional<User> user = userRepository.findById(id);
		List<Animal> userAnimals = new ArrayList<Animal>();
		if (user.isPresent()) {
			userAnimals = user.get().getAnimals();
		}
		return userAnimals;
	}

	/**
	 * Creates an {@link Animal} and add it to the list of given {@link User}
	 * 
	 * @param dto
	 * @return
	 * @throws CannotCreateException
	 */
	public User addAnimalToUser(AnimalWithUserId dto) throws CannotCreateException {
		User user = checkIfUserIsPresent(dto.getUserId());
		checkHowManyAnimalsUsersGot(user);
		Animal animal = Animal.of(dto);
		animalRepository.save(animal);
		user.getAnimals().add(animal);
		return userRepository.save(user);
	}

	/**
	 * Check how many animals a {@link User} has, maximum authorized is 3, if answer id more than 3 it throw an exception
	 * 
	 * @param user {@link User}
	 * @throws CannotCreateException
	 */
	public void checkHowManyAnimalsUsersGot(User user) throws CannotCreateException {
		List<Animal> usersAnimals = user.getAnimals();
		if (usersAnimals != null && usersAnimals.size() > 2) {
			throw new CannotCreateException("User already have three animals created");
		}
	}

	/** Update an existing User 
	 * @param uifdto {@link UserInfoFullDto}
	 * @return {@link User}
	 */
	public User updateUser(UserInfoFullDto uifdto) {
		User fromBase = checkIfUserIsPresent(uifdto.getId());

		if (fromBase.getEmail().equals(uifdto.getEmail())) {
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

	/** Check if user has registered some animals
	 * @param id
	 * @return {@link Boolean}
	 */
	public boolean checkIfUserHasAnimals(UUID id) {
		User user = checkIfUserIsPresent(id);
		boolean response = false;
		if (user.getAnimals() != null && user.getAnimals().size() > 0 && !user.getAnimals().isEmpty()) {
			response = true;
		}
		return response;
	}

	/**
	 * Checks if the profile is complete, or if it's missing some informations
	 * 
	 * @param id
	 * @return
	 */
	public boolean isProfileComplete(UUID id) {
		User user = checkIfUserIsPresent(id);
		return false;
	}

	
	/** 
	 * Simple check in database to see if User from given id is present
	 * 
	 * @param id
	 * @return User from given ID, or throw a {@link UserNotFoundException}
	 */
	private User checkIfUserIsPresent(UUID id) {
		Optional<User> optionalUser = userRepository.findById(id);
		if (!optionalUser.isPresent()) {
			throw new UserNotFoundException("User not found");
		}
		User user = optionalUser.get();
		return user;
	}

}

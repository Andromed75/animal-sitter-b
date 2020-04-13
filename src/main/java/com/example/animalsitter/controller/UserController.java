package com.example.animalsitter.controller;

import java.util.List;
import java.util.Optional;
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

import com.example.animalsitter.domain.Disponibility;
import com.example.animalsitter.domain.Indisponibility;
import com.example.animalsitter.domain.User;
import com.example.animalsitter.dto.request.AnimalWithUserId;
import com.example.animalsitter.dto.request.StartAnEndWithUserId;
import com.example.animalsitter.exception.UserNotFoundException;
import com.example.animalsitter.repository.DisponibilityRepository;
import com.example.animalsitter.repository.IndisponibilityRepo;
import com.example.animalsitter.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@RestController
@CrossOrigin
@RequestMapping("api/v1/user")
@Slf4j
public class UserController {

	@Autowired
	UserRepository userRepo;

	@Autowired
	DisponibilityRepository dispoRepo;

	@Autowired
	IndisponibilityRepo indspoRepo;

	@PreAuthorize("hasRole('ROLE_USER')")
	@GetMapping("/{id}")
	public ResponseEntity<User> getUserById(@PathVariable("id") UUID id) {
		return ResponseEntity.ok(userRepo.findById(id).get());
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping()
	public ResponseEntity<List<User>> getUsers() {
		return ResponseEntity.ok(userRepo.findAll());
	}

	@PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_MODERATOR') or hasRole('ROLE_ADMIN')")
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteById(@PathVariable("id") UUID id) {
		userRepo.deleteById(id);
		return ResponseEntity.ok().build();
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@PostMapping("/add-disponibility")
	public ResponseEntity<User> addDisponibilityToUser(@RequestBody StartAnEndWithUserId dto) {

		Optional<User> optionalUser = userRepo.findById(dto.getId());
		if (!optionalUser.isPresent()) {
			log.info("User with id = {} not found", dto.getId());
			throw new UserNotFoundException("User not found");
		}
		User user = null;
		Disponibility disponiility = Disponibility.of(dto);
		dispoRepo.save(disponiility);
		user = optionalUser.get();
		user.getDisponibility().add(disponiility);
		userRepo.save(user);

		return ResponseEntity.ok(user);
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@PostMapping("/add-indisponibility")
	public ResponseEntity<Disponibility> addIndisponibility(@RequestBody StartAnEndWithUserId dto) {

		Optional<Disponibility> optionalDispo = dispoRepo.findById(dto.getId());
		if (!optionalDispo.isPresent()) {
			log.info("Disponibility with id = {} not found", dto.getId());
			throw new UserNotFoundException("Disponibility not found");
		}
		Disponibility disponibility = null;

		Indisponibility indisponiility = Indisponibility.of(dto);
		indspoRepo.save(indisponiility);
		disponibility = optionalDispo.get();
		disponibility.getIndisponibility().add(indisponiility);
		indspoRepo.save(indisponiility);

		return ResponseEntity.ok(disponibility);
	}

	public ResponseEntity<User> addAnimalToUser(@RequestBody AnimalWithUserId dto) {
		Optional<User> optionalUser = userRepo.findById(dto.getUserId());
		if (!optionalUser.isPresent()) {
			log.info("User with id = {} not found", dto.getUserId());
			throw new UserNotFoundException("User not found");
		}
		User user = null;
		

		return null;
	}

}

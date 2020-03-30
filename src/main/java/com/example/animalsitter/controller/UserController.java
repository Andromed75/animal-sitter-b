package com.example.animalsitter.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.animalsitter.domain.User;
import com.example.animalsitter.repository.UserRepository;

@RestController
@CrossOrigin
@RequestMapping("api/v1/user")
public class UserController {

	@Autowired
	UserRepository userRepo;
	
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
	
	
	
	
}

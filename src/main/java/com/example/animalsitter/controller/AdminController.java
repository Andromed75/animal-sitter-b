package com.example.animalsitter.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.animalsitter.domain.Admin;
import com.example.animalsitter.domain.Animal;
import com.example.animalsitter.domain.Sitting;
import com.example.animalsitter.domain.User;
import com.example.animalsitter.dto.UserDto;
import com.example.animalsitter.service.AdminService;
import com.example.animalsitter.service.AuthService;

@RequestMapping("/api/v1/admin")
@CrossOrigin
@RestController
public class AdminController {

	@Autowired
	AdminService adminService;
	
	@Autowired
	AuthService authService;
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/all-users")
	public ResponseEntity<List<User>> findAllUsers() {
		return ResponseEntity.ok(adminService.findAllUsers());
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/all-animals")
	public ResponseEntity<List<Animal>> findAllAnimals() {
		return ResponseEntity.ok(adminService.findAllAnimals());
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/all-sittings")
	public ResponseEntity<List<Sitting>> findAllSittings() {
		return ResponseEntity.ok(adminService.findAllSittings());
	}
	
	// @PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("/create")
	public ResponseEntity<Admin> createAdmin(@RequestBody UserDto dto) {
		return ResponseEntity.ok(authService.createAdmin(dto));
	}
}

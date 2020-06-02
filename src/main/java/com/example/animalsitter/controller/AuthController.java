package com.example.animalsitter.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.animalsitter.domain.Address;
import com.example.animalsitter.domain.Animal;
import com.example.animalsitter.domain.Disponibility;
import com.example.animalsitter.domain.Role;
import com.example.animalsitter.domain.User;
import com.example.animalsitter.dto.UserDto;
import com.example.animalsitter.dto.request.LoginRequest;
import com.example.animalsitter.dto.response.JwtResponse;
import com.example.animalsitter.enums.ERole;
import com.example.animalsitter.repository.DisponibilityRepository;
import com.example.animalsitter.repository.IndisponibilityRepo;
import com.example.animalsitter.repository.RoleRepository;
import com.example.animalsitter.repository.UserRepository;
import com.example.animalsitter.security.jwt.JwtUtils;
import com.example.animalsitter.security.service.UserDetailsImpl;
import com.example.animalsitter.service.AuthService;
import com.example.animalsitter.service.MailJetService;
import com.example.animalsitter.service.UserService;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@CrossOrigin
@RequestMapping("/api/v1/auth")
public class AuthController {
	
	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtUtils jwtUtils;

	@Autowired
	DisponibilityRepository dispoRepo;

	@Autowired
	UserRepository userRepo;

	@Autowired
	IndisponibilityRepo indispoRepo;
	
	@Autowired
	MailJetService mjs;
	
	@Autowired
	AuthService authService;
	

	
	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
		log.info("HTTP HANDLING : authenticateUser with user : {}", loginRequest.getPassword());
		

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);
		
		
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();		
		List<String> roles = userDetails.getAuthorities().stream()
				.map(item -> item.getAuthority())
				.collect(Collectors.toList());

		return ResponseEntity.ok(new JwtResponse(jwt, 
												 userDetails.getId(), 
												 userDetails.getUsername(), 
												 userDetails.getEmail(), 
												 roles));
	}
	
	@PostMapping("/signup")
	public ResponseEntity<?> createUser(@Valid @RequestBody UserDto userDto) {

		if (userRepository.existsByPseudo(userDto.getPseudo())) {
			return ResponseEntity
					.badRequest()
					.body("Ce pseudo est déjà utilisé ! ");
		}

		if (userRepository.existsByEmail(userDto.getEmail())) {
			return ResponseEntity
					.badRequest()
					.body("Cet e-mail est déjà utilisé !");
		}

		User user = authService.createNewUser(userDto);
		return ResponseEntity.ok(user);
	}
	
	
}

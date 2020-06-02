package com.example.animalsitter.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.animalsitter.domain.Address;
import com.example.animalsitter.domain.Animal;
import com.example.animalsitter.domain.Disponibility;
import com.example.animalsitter.domain.Role;
import com.example.animalsitter.domain.User;
import com.example.animalsitter.dto.UserDto;
import com.example.animalsitter.enums.ERole;
import com.example.animalsitter.repository.RoleRepository;
import com.example.animalsitter.repository.UserRepository;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;

import lombok.extern.slf4j.Slf4j;

/**
 * This service is dedicated to the authentication of user
 * 
 * @author ae.de-donno
 *
 */
@Service
@Slf4j
public class AuthService {
	
	UserRepository userRepository;
	
	RoleRepository roleRepository;
	
	PasswordEncoder encoder;
	
	@Autowired
	MailJetService mjs;
	
	@Autowired
	public AuthService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder encoder) {
		this.roleRepository = roleRepository;
		this.userRepository = userRepository;
		this.encoder = encoder;
	}

	/**
	 * Creates and send a mail to the user
	 * 
	 * @param userDto
	 * @return
	 */
	public User createNewUser(UserDto userDto) {
		User user = createUser(userDto);
		
		try {
			mjs.sendMail(user, mjs.accountCreationMessage(user.getPseudo()), MailJetService.SUBJECT_CREATION);
		} catch (MailjetException | MailjetSocketTimeoutException e) {
			log.info("Error sending mail :{}", e.getMessage());
		}
		return user;
	}

	/**
	 * This method creates a new {@link User} with a ROLE_USER role
	 * 
	 * @param userDto
	 * @return a new user saved and created
	 */
	public User createUser(UserDto userDto) {
		Role userRole = roleRepository.findByName(ERole.ROLE_USER).orElseThrow(() -> new RuntimeException("ERROR ROLE IS NOT FOUND"));
		Set<Role> roles = new HashSet<>();
		roles.add(userRole);
		User user = new User(null, userDto.getPseudo(), encoder.encode(userDto.getPassword()), userDto.getEmail(), new ArrayList<Animal>(), new ArrayList<Disponibility>(), roles, new Address(), new ArrayList<Integer>());
		userRepository.save(user);
		return user;
	}
	
}

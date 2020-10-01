package com.example.animalsitter.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.animalsitter.domain.Animal;
import com.example.animalsitter.domain.Sitting;
import com.example.animalsitter.domain.User;
import com.example.animalsitter.repository.Animalrepository;
import com.example.animalsitter.repository.SittingRepository;
import com.example.animalsitter.repository.UserRepository;

@Service
public class AdminService {
	
	UserRepository userRepo;
	Animalrepository animalRepo;
	SittingRepository sittingRepo;
	
	@Autowired
	public AdminService(UserRepository userRepo, Animalrepository animalRepo, SittingRepository sittingRepo) {
		super();
		this.userRepo = userRepo;
		this.animalRepo = animalRepo;
		this.sittingRepo = sittingRepo;
	}

	
	/**
	 * @return all users
	 */
	public List<User> findAllUsers() {
		return userRepo.findAll();
	}
	
	/**
	 * @return all animals
	 */
	public List<Animal> findAllAnimals(){
		return animalRepo.findAll();
	}
	
	/**
	 * @return all Sittings
	 */
	public List<Sitting> findAllSittings(){
		return sittingRepo.findAll();
	}
	
}

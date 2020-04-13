package com.example.animalsitter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.animalsitter.controller.DisponibilityController;
import com.example.animalsitter.repository.DisponibilityRepository;

@Service
public class DisponibilityService {

	@Autowired
	DisponibilityRepository dispoRepo;
	
	
	
}

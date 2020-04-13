package com.example.animalsitter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.animalsitter.service.DisponibilityService;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/disponibility")
public class DisponibilityController {

	@Autowired
	DisponibilityService dispoService;
	
	
	
}

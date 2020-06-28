package com.example.animalsitter.controller;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.animalsitter.domain.Photo;
import com.example.animalsitter.repository.PhotoRepository;
import com.example.animalsitter.service.PhotoService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@CrossOrigin
@RequestMapping("/api/v1/photo")
public class PhotoController {
	
	PhotoService photoService;
	
	PhotoRepository photoRepo;
	
	@Autowired
	public PhotoController(PhotoService photoService, PhotoRepository photoRepo) {
		this.photoService = photoService;
		this.photoRepo = photoRepo;
	}
	
	
	@PostMapping("/upload/user/{id}")
	public BodyBuilder uploadUserImage(@RequestParam("imageFile") MultipartFile file, @PathVariable("id") UUID id) throws IOException {
		log.info("Http handling uploadUserImage");
		log.info("Original Image Byte Size - " + file.getBytes().length);
		photoService.saveAnimalPhoto(id, file);		
		return ResponseEntity.status(HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@PostMapping("/upload/animal/{id}")
	public BodyBuilder uploadAnimalImage(@RequestParam("imageFile") MultipartFile file, @PathVariable("id") UUID id) throws IOException {
		log.info("Http handling uploadAnimalImage");
		log.info("Original Image Byte Size - " + file.getBytes().length);
		photoService.saveAnimalPhoto(id, file);
		return ResponseEntity.status(HttpStatus.OK);
	}
	
	@GetMapping("/get/{id}")
	public ResponseEntity<Photo> getImage(@PathVariable("id") UUID id) throws IOException {
		log.info("Http handling getImage with id = {}", id);
		return ResponseEntity.ok(photoService.getAnimalImage(id));
	}
	
}

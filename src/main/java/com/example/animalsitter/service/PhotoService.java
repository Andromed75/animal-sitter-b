package com.example.animalsitter.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.animalsitter.domain.Animal;
import com.example.animalsitter.domain.Photo;
import com.example.animalsitter.domain.User;
import com.example.animalsitter.exception.AnimalNotFoundException;
import com.example.animalsitter.exception.PhotoNotFoundException;
import com.example.animalsitter.repository.Animalrepository;
import com.example.animalsitter.repository.PhotoRepository;
import com.example.animalsitter.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PhotoService {

	PhotoRepository photoRepo;

	UserRepository userRepo;

	Animalrepository animalRepo;

	public PhotoService(PhotoRepository photoRepo, UserRepository userRepo, Animalrepository animalRepo) {
		this.photoRepo = photoRepo;
		this.userRepo = userRepo;
		this.animalRepo = animalRepo;
	}

	public void saveUserPhoto(UUID userId) {
		Optional<User> optionalUser = userRepo.findById(userId);
		if (!optionalUser.isPresent()) {
			throw new UsernameNotFoundException("User not found");
		}
		User user = optionalUser.get();
	}

	public void saveAnimalPhoto(UUID animalId, MultipartFile file) {
		Animal animal = getAnimalControlled(animalId);
		Optional<Photo> optionalPhoto = photoRepo.findById(animal.getPhoto().getId());
		if(!optionalPhoto.isPresent()) {
			throw new PhotoNotFoundException("Photo was not found");
		}
		Photo photo = optionalPhoto.get();
		log.info("photo id : {}", photo.getId());
		photo.setName(file.getOriginalFilename());
		photo.setType(file.getContentType());
		try {
			photo.setImage(compressBytes(file.getBytes()));
		} catch (IOException e) {
			log.info("Error compressing file : {}", e.getMessage());
		}
		photoRepo.save(photo);
	}

	public Photo getAnimalImage(UUID id) {
		Animal animal = getAnimalControlled(id);
		Photo photo = new Photo(animal.getPhoto().getId(), animal.getPhoto().getName(), animal.getPhoto().getType(), decompressBytes(animal.getPhoto().getImage()));
		return photo;
	}

	// compress the image bytes before storing it in the database
	public byte[] compressBytes(byte[] data) {
		Deflater deflater = new Deflater();
		deflater.setInput(data);
		deflater.finish();
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
		byte[] buffer = new byte[1024];
		while (!deflater.finished()) {
			int count = deflater.deflate(buffer);
			outputStream.write(buffer, 0, count);
		}
		try {
			outputStream.close();
		} catch (IOException e) {
		}
		System.out.println("Compressed Image Byte Size - " + outputStream.toByteArray().length);
		return outputStream.toByteArray();
	}

	// uncompress the image bytes before returning it to the angular application
	public byte[] decompressBytes(byte[] data) {
		Inflater inflater = new Inflater();
		inflater.setInput(data);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
		byte[] buffer = new byte[1024];
		try {
			while (!inflater.finished()) {
				int count = inflater.inflate(buffer);
				outputStream.write(buffer, 0, count);
			}
			outputStream.close();
		} catch (IOException ioe) {
		} catch (DataFormatException e) {
		}
		return outputStream.toByteArray();
	}

	private Animal getAnimalControlled(UUID animalId) {
		Optional<Animal> optionalAnimal = animalRepo.findById(animalId);
		if (!optionalAnimal.isPresent()) {
			throw new AnimalNotFoundException("Animal not found");
		}
		Animal animal = optionalAnimal.get();
		return animal;
	}
}

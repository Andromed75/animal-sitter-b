package com.example.animalsitter.service;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.animalsitter.domain.Animal;
import com.example.animalsitter.domain.Sitting;
import com.example.animalsitter.domain.Status;
import com.example.animalsitter.domain.User;
import com.example.animalsitter.dto.SittingDto;
import com.example.animalsitter.dto.SittingToShowDto;
import com.example.animalsitter.enums.StatusEnum;
import com.example.animalsitter.exception.UserNotFoundException;
import com.example.animalsitter.repository.Animalrepository;
import com.example.animalsitter.repository.SittingRepository;
import com.example.animalsitter.repository.StatusRepository;
import com.example.animalsitter.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * @author ae.de-donno
 *
 *
 * Service related to {@link Sitting}}
 */
@Service
@Slf4j
public class SittingService {

	
	UserRepository userRepository;
	
	SittingRepository sittingRepo;
	
	StatusRepository statusRepo;
	
	Animalrepository animalRepo;
	
	PhotoService photoService;
	
	@Autowired
	public SittingService(UserRepository userRepository, SittingRepository sittingRepo, StatusRepository statusRepo, Animalrepository animalRepo, PhotoService photoService) {
		this.userRepository = userRepository;
		this.sittingRepo = sittingRepo;
		this.statusRepo = statusRepo;
		this.animalRepo = animalRepo;
		this.photoService = photoService;
		
	}

	/**
	 * @param dto
	 * @return
	 */
	@Transactional
	public Sitting createSitting(SittingDto dto) {
		
			Sitting sitting = new Sitting();
			
			// Set animal
			Animal animal = animalRepo.getOne(dto.getAnimalId());
			sitting.setAnimal(animal);
			
			sitting.setTitle(dto.getTitle());
			sitting.setUserId(dto.getUserId());
			sitting.setDescription(dto.getDescription());
			
			List<Status> statusList = new ArrayList<>();
			sitting.setStatus(statusList);
			sittingRepo.save(sitting);
			Status status = Status.builder().date(LocalDateTime.now(Clock.systemUTC())).status(StatusEnum.STATUS_ONE).build();
			statusRepo.save(status);
			statusList.add(status);
			sitting.setStatus(statusList);
			
			// Manage dates
			sitting.setCreatedDate(LocalDate.now(Clock.systemUTC()));
			DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withLocale(Locale.FRENCH);
			LocalDateTime beg = LocalDateTime.parse(dto.getShiftBeggining(), inputFormatter);
			LocalDateTime end = LocalDateTime.parse(dto.getShiftEnd(), inputFormatter);
			log.info("BEGGINING : {}, END : {}", beg, end);
			sitting.setShiftBeggining(beg);
			sitting.setShiftEnd(end);
			
			User user = checkIfUserIsPresent(dto.getUserId());
			sittingRepo.save(sitting);
			user.getSittings().add(sitting);
			userRepository.save(user);
			return sitting;
		
	}

	/**
	 * @param id
	 * @return a {@link SittingToShowDto} with a {@link Sitting} ID
	 */  
	public SittingToShowDto getById(UUID id) {
		SittingToShowDto response = sittingRepo.findSittingById(id);
		return response;
	}
	
	/** 
	 * Simple check in database to see if User from given id is present
	 * 
	 * @param id
	 * @return User from given ID, or throw a {@link UserNotFoundException}
	 */
	private User checkIfUserIsPresent(UUID id) {
		Optional<User> optionalUser = userRepository.findById(id);
		if (!optionalUser.isPresent()) {
			throw new UserNotFoundException("User not found");
		}
		User user = optionalUser.get();
		return user;
	}


	/**
	 * @param postcode
	 * @param page
	 * @return
	 */
	public List<SittingToShowDto> findAllByPostcodePaginated(String postcode, int page) {
		Pageable pageable = PageRequest.of(page, 4);
		List<SittingToShowDto> sittingList = sittingRepo.findAllByPostcodePaginated(postcode, pageable);
		return sittingList;
	}
	
	/**
	 * @param postcode
	 * @param page
	 * @param sittingBeg
	 * @param sittingEnd
	 * @return a List of {@link SittingToShowDto}
	 */
	public List<SittingToShowDto> findAllByPostcodePaginatedWithDates(String postcode, int page, String sittingBeg, String sittingEnd) {
		Pageable pageable = PageRequest.of(page, 4);
		DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withLocale(Locale.FRENCH);
		LocalDateTime beg = LocalDateTime.parse(sittingBeg, inputFormatter);
		LocalDateTime end = LocalDateTime.parse(sittingEnd, inputFormatter);
		List<SittingToShowDto> sittingList = sittingRepo.findAllByPostcodePaginatedWithDate(postcode, beg, end, pageable);
		return sittingList;
	}

	public List<Sitting> getUserSittings(UUID id) {
		Optional<User> optionalUser = userRepository.findById(id);
		if(!optionalUser.isPresent()) {
			throw new UserNotFoundException("User with id : " + id.toString() + " not found");
		}
		List<Sitting> userSittings = optionalUser.get().getSittings();
		return userSittings;
	}

	public void deleteSitting(UUID id) {
		sittingRepo.deleteById(id);
	}

	public List<SittingToShowDto> findAllByPostcodeWithDates(String postcode, String sittingBeg, String sittingEnd) {
		DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withLocale(Locale.FRENCH);
		LocalDateTime beg = LocalDateTime.parse(sittingBeg, inputFormatter);
		LocalDateTime end = LocalDateTime.parse(sittingEnd, inputFormatter);
		List<SittingToShowDto> sittingList = sittingRepo.findAllByPostcodeWithDate(postcode, beg, end);
		return sittingList;
	}
	
}

package com.example.animalsitter.service;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.animalsitter.domain.Animal;
import com.example.animalsitter.domain.Disponibility;
import com.example.animalsitter.domain.Indisponibility;
import com.example.animalsitter.domain.Sitting;
import com.example.animalsitter.domain.Status;
import com.example.animalsitter.domain.User;
import com.example.animalsitter.dto.DisponibilityDTO;
import com.example.animalsitter.dto.SearchSittingDto;
import com.example.animalsitter.dto.SittingDto;
import com.example.animalsitter.dto.SittingToShowDto;
import com.example.animalsitter.enums.StatusEnum;
import com.example.animalsitter.exception.SittingNotFoundException;
import com.example.animalsitter.exception.UserNotFoundException;
import com.example.animalsitter.repository.Animalrepository;
import com.example.animalsitter.repository.SittingRepository;
import com.example.animalsitter.repository.StatusRepository;
import com.example.animalsitter.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SittingService {

	
	UserRepository userRepository;
	
	SittingRepository sittingRepo;
	
	StatusRepository statusRepo;
	
	Animalrepository animalRepo;
	
	@Autowired
	public SittingService(UserRepository userRepository, SittingRepository sittingRepo, StatusRepository statusRepo, Animalrepository animalRepo) {
		this.userRepository = userRepository;
		this.sittingRepo = sittingRepo;
		this.statusRepo = statusRepo;
		this.animalRepo = animalRepo;
		
	}
	
	public static void main(String[] args) {
		ZoneOffset zos = ZoneOffset.ofHours(1);
		OffsetDateTime ldt = OffsetDateTime.of(2020, 2, 3, 0, 0, 0, 0, zos);
		System.out.println(ldt.toString());
	}
	
//	public List<User> search(DisponibilityDTO dispo) {
//		DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
//		OffsetDateTime start = LocalDateTime.parse(dispo.getBeggining(), inputFormatter)
//				.atZone(ZoneId.of("Europe/Paris")).plusHours(1).toOffsetDateTime();
//		OffsetDateTime end = LocalDateTime.parse(dispo.getEnd(), inputFormatter).atZone(ZoneId.of("Europe/Paris"))
//				.plusHours(1).toOffsetDateTime();
//		List<User> allUsers = userRepository.findAll();
//		List<User> response = new ArrayList<User>();
//		for (User u : allUsers) {
//			log.info("For user u = {}", u);
//			for (Sitting s : u.getSittings()) {
//				log.info("Dispo d = {}", s);
//				boolean shiftBegCompare = start.compareTo(s.getShiftBeggining()) >= 0;
//				boolean shiftEndCompare = end.compareTo(s.getShiftEnd()) <= 0;
//				log.info("COMPARE beg = {}, end = {}", shiftBegCompare, shiftEndCompare);
//				if (shiftBegCompare && shiftEndCompare) {
//					log.info("PREMIER IF TRUE");
//					if (s.getIndisponibility().isEmpty()) {
//						log.info("INDISPO VIDE");
//						response.add(u);
//					} else {
//						for (Indisponibility i : d.getIndisponibility()) {
//							log.info("start = {}, startIndispo = {}", start, i.getShiftBeggining());
//							log.info("end = {}, endIndispo = {}", end, i.getShiftEnd());
//
//							// TODO : FAIRE UNE METHODE INBETWEEN QUI PREND EN PARAMETRE DEUX DATES POUR LES
//							// COMPARER
//							boolean startNotBetweenIndispo = start.compareTo(i.getShiftBeggining()) >= 0
//									&& start.compareTo(i.getShiftEnd()) <= 0;
//							boolean endNotBetweenIndispo = end.compareTo(i.getShiftBeggining()) >= 0
//									&& end.compareTo(i.getShiftEnd()) <= 0;
//							boolean indispoStartNotBetweenWanted = i.getShiftBeggining().compareTo(start) >= 0
//									&& i.getShiftBeggining().compareTo(end) <= 0;
//							boolean indispoEndNotBetweenWanted = i.getShiftEnd().compareTo(start) >= 0
//									&& i.getShiftEnd().compareTo(end) <= 0;
//							log.info("INDISPO REMPLI beg = {}, end = {}", startNotBetweenIndispo, endNotBetweenIndispo);
//							if (!startNotBetweenIndispo && !endNotBetweenIndispo && !indispoEndNotBetweenWanted
//									&& !indispoStartNotBetweenWanted) {
//								response.add(u);
//							}
//						}
//					}
//
//				}
//			}
//		}
//		allUsers.stream().forEach(u -> u.getDisponibility().stream().filter(d -> (d.getShiftBeggining().compareTo(start) >= 0 && d.getShiftEnd().compareTo(end) <= 0) ).forEach(d -> d.getIndisponibility()
//		.stream().filter(i -> i.getShiftBeggining().compareTo(start) < 0 && i.getShiftEnd().compareTo(end) > 0).forEach(i -> response.add(u))));
//		return response;
//	}

	@Transactional
	public Sitting createSitting(SittingDto dto) {
			Sitting sitting = new Sitting();
//			sitting.setAnimalId(dto.getAnimalId());
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
			sitting.setCreatedDate(LocalDate.now(Clock.systemUTC()));
			DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			OffsetDateTime beg = LocalDateTime.parse(dto.getShiftBeggining(), inputFormatter).atZone(ZoneId.of("Europe/Paris")).toOffsetDateTime();
			OffsetDateTime end = LocalDateTime.parse(dto.getShiftEnd(), inputFormatter).atZone(ZoneId.of("Europe/Paris")).toOffsetDateTime();
			log.info("BEGGINING : {}, END : {}", beg, end);
			sitting.setShiftBeggining(beg);
			sitting.setShiftEnd(end);
			User user = checkIfUserIsPresent(dto.getUserId());
			sittingRepo.save(sitting);
			user.getSittings().add(sitting);
			userRepository.save(user);
			return sitting;
		
	}

	public Sitting getById(UUID id) {
		Optional<Sitting> sittingOptional = sittingRepo.findById(id);
		if(!sittingOptional.isPresent()) {
			throw new SittingNotFoundException("Sitting not not found");
		}
		return sittingOptional.get();
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

	public List<SittingToShowDto> findAllSittingsForWebApp(SearchSittingDto dto) {
		
		List<SittingToShowDto> response = new ArrayList<>();
		List<User> userList = userRepository.findAll();
//		List<User> userList = userRepository.findAllByPostcode(dto.getPostcode());
//		for (User u : userList) {
//			if(dto.getPostcode().equals(u.getAdress().getPostalcode())) {
//				for (Sitting s : u.getSittings()) {
//					
//				}
//			}
//		}
		
		return null;
	}

	public List<SittingToShowDto> findAllByPostcodePaginated(String postcode, int page) {
		Pageable pageable = PageRequest.of(page, 4);
		return sittingRepo.findAllByPostcodePaginated(postcode, pageable);
	}
}

package com.example.animalsitter.controller;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.animalsitter.domain.Animal;
import com.example.animalsitter.domain.Disponibility;
import com.example.animalsitter.domain.Indisponibility;
import com.example.animalsitter.domain.Role;
import com.example.animalsitter.domain.User;
import com.example.animalsitter.dto.DisponibilityDTO;
import com.example.animalsitter.dto.UserDto;
import com.example.animalsitter.enums.ERole;
import com.example.animalsitter.repository.DisponibilityRepository;
import com.example.animalsitter.repository.IndisponibilityRepo;
import com.example.animalsitter.repository.RoleRepository;
import com.example.animalsitter.repository.UserRepository;
import com.example.animalsitter.security.jwt.JwtUtils;

import lombok.extern.slf4j.Slf4j;

@RestController
@CrossOrigin
@RequestMapping("api/sitting/v1")
@Slf4j
public class SittingController {
	
	private static final String ERROR_ROLE_IS_NOT_FOUND = "Error: Role is not found.";

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

	@PostMapping
	public void testOffset(@RequestBody DisponibilityDTO dispo) {
		// OffsetDateTime ldt = OffsetDateTime.of(odd.getYear(), odd.getMonth(),
		// odd.getDayOfMonth(), 0, 0, 0, 0, zos);
		DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		// DateTimeFormatter outputFormatter =
		// DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SS", Locale.FRANCE);
		// OffsetDateTime date = OffsetDateTime.parse(odd.getLocal(), inputFormatter ,
		// ZoneId.);
		OffsetDateTime beg = LocalDateTime.parse(dispo.getBeggining(), inputFormatter).atZone(ZoneId.of("Europe/Paris"))
				.plusHours(1).toOffsetDateTime();
		OffsetDateTime end = LocalDateTime.parse(dispo.getEnd(), inputFormatter).atZone(ZoneId.of("Europe/Paris"))
				.plusHours(1).toOffsetDateTime();
		log.info("beg = {}, end = {}", beg, end);
		Disponibility d = Disponibility.of(dispo);
		log.info("Dispo construit = {}", d);
		dispoRepo.save(d);
		log.info("Après SAVE = {}", d);
	}

	@GetMapping
	public ResponseEntity<List<Disponibility>> getAllDispo() {
		List<Disponibility> dispo = dispoRepo.findAll();
		dispo.stream().forEach(x -> {
			x.setShiftBeggining(x.getShiftBeggining().atZoneSameInstant(ZoneId.of("Europe/Paris")).toOffsetDateTime());
			x.setShiftEnd(x.getShiftEnd().atZoneSameInstant(ZoneId.of("Europe/Paris")).toOffsetDateTime());
		});
		return ResponseEntity.ok(dispo);
	}

	@GetMapping("/test-date/{id}")
	public boolean testDeGrandeur(@PathVariable("id") UUID id) {
		Disponibility d = dispoRepo.findById(id).get();
		boolean x = false;

		if (d.getShiftBeggining().compareTo(d.getShiftEnd()) > 0) {
			x = true;
			log.info("made true");
		}
		if (d.getShiftBeggining().compareTo(d.getShiftEnd()) < 0) {
			x = false;
			log.info("made false");
		}

		return x;
	}

//	@PostMapping("/indispo")
//	public void insertInDispo(@RequestBody IndispoDto indispo) {
//		Disponibility d = dispoRepo.findById(UUID.fromString(indispo.getDispoUUID())).get();
//		Indisponibility i = Indisponibility.of(indispo);
//		d.getIndisponibility().add(i);
//		dispoRepo.save(d);
//	}

	@PostMapping("/verify")
	public List<User> verify(@RequestBody DisponibilityDTO dispo) {
		boolean x = false;
		DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		OffsetDateTime start = LocalDateTime.parse(dispo.getBeggining(), inputFormatter)
				.atZone(ZoneId.of("Europe/Paris")).plusHours(1).toOffsetDateTime();
		OffsetDateTime end = LocalDateTime.parse(dispo.getEnd(), inputFormatter).atZone(ZoneId.of("Europe/Paris"))
				.plusHours(1).toOffsetDateTime();
		List<User> allUsers = userRepo.findAll();
		List<User> response = new ArrayList<User>();
		for (User u : allUsers) {
			log.info("For user u = {}", u);
			for (Disponibility d : u.getDisponibility()) {
				log.info("Dispo d = {}", d);
				boolean shiftBegCompare = start.compareTo(d.getShiftBeggining()) >= 0;
				boolean shiftEndCompare = end.compareTo(d.getShiftEnd()) <= 0;
				log.info("COMPARE beg = {}, end = {}", shiftBegCompare, shiftEndCompare);
				if (shiftBegCompare && shiftEndCompare) {
					log.info("PREMIER IF TRUE");
					if (d.getIndisponibility().isEmpty()) {
						log.info("INDISPO VIDE");
						response.add(u);
					} else {
						for (Indisponibility i : d.getIndisponibility()) {
							log.info("start = {}, startIndispo = {}", start, i.getShiftBeggining());
							log.info("end = {}, endIndispo = {}", end, i.getShiftEnd());

							// TODO : FAIRE UNE METHODE INBETWEEN QUI PREND EN PARAMETRE DEUX DATES POUR LES
							// COMPARER
							boolean startNotBetweenIndispo = start.compareTo(i.getShiftBeggining()) >= 0
									&& start.compareTo(i.getShiftEnd()) <= 0;
							boolean endNotBetweenIndispo = end.compareTo(i.getShiftBeggining()) >= 0
									&& end.compareTo(i.getShiftEnd()) <= 0;
							boolean indispoStartNotBetweenWanted = i.getShiftBeggining().compareTo(start) >= 0
									&& i.getShiftBeggining().compareTo(end) <= 0;
							boolean indispoEndNotBetweenWanted = i.getShiftEnd().compareTo(start) >= 0
									&& i.getShiftEnd().compareTo(end) <= 0;
							log.info("INDISPO REMPLI beg = {}, end = {}", startNotBetweenIndispo, endNotBetweenIndispo);
							if (!startNotBetweenIndispo && !endNotBetweenIndispo && !indispoEndNotBetweenWanted
									&& !indispoStartNotBetweenWanted) {
								response.add(u);
							}
						}
					}

				}
			}
		}

//		allUsers.stream().forEach(u -> u.getDisponibility().stream().filter(d -> (d.getShiftBeggining().compareTo(start) >= 0 && d.getShiftEnd().compareTo(end) <= 0) ).forEach(d -> d.getIndisponibility()
//				.stream().filter(i -> i.getShiftBeggining().compareTo(start) < 0 && i.getShiftEnd().compareTo(end) > 0).forEach(i -> response.add(u))));

		return response;
	}

	

	@PostMapping("/add-dispo/{id}")
	public void addDisponibilityToUser(@RequestBody DisponibilityDTO dispo, @PathVariable("id") String id) {

		Disponibility d = createDispoFromDto(dispo);
		User u = userRepo.findById(UUID.fromString(id)).get();
		u.getDisponibility().add(d);
		userRepo.save(u);
		log.info("User u = {}, Dispo d = {}, d set to u", u, d);
	}

	@Transactional
	public Disponibility createDispoFromDto(DisponibilityDTO dispo) {
		Disponibility d = Disponibility.of(dispo);
		dispoRepo.save(d);
		return d;
	}

	@PostMapping("/add-indispo/{id}")
	public void addIndisponibilityToUser(@RequestBody DisponibilityDTO dispo, @PathVariable("id") String id) {

		Indisponibility i = Indisponibility.of(dispo);
		// indispoRepo.save(i);
		Disponibility d = dispoRepo.findById(UUID.fromString(id)).get();
		d.getIndisponibility().add(i);
		dispoRepo.save(d);
		log.info("Dispo d = {}, Indispo i = {}, i set to d", d, i);
	}

	@DeleteMapping("/all-dispos")
	public void deleteAllDispos() {
		dispoRepo.deleteAll();
		log.info("ALL DISPO DELETES");
	}

	@GetMapping("/find-all-users")
	public ResponseEntity<List<User>> getAllUsers() {
		return ResponseEntity.ok(userRepo.findAll());
	}

}

package com.example.animalsitter.controller;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.animalsitter.domain.Disponibility;
import com.example.animalsitter.domain.Indisponibility;
import com.example.animalsitter.domain.Sitting;
import com.example.animalsitter.domain.User;
import com.example.animalsitter.dto.DisponibilityDTO;
import com.example.animalsitter.dto.SittingDto;
import com.example.animalsitter.dto.SittingToShowDto;
import com.example.animalsitter.repository.DisponibilityRepository;
import com.example.animalsitter.repository.IndisponibilityRepo;
import com.example.animalsitter.repository.RoleRepository;
import com.example.animalsitter.repository.SittingRepository;
import com.example.animalsitter.repository.UserRepository;
import com.example.animalsitter.security.jwt.JwtUtils;
import com.example.animalsitter.service.SittingService;

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
	
	@Autowired
	SittingService sittingService;
	
	@Autowired
	SittingRepository sittingRepo;
	
//	public ResponseEntity<List<SittingToShowDto>> getSittingsForWebApp(@RequestBody SearchSittingDto dto) {
//		
//		return ResponseEntity.ok(sittingService.findAllSittingsForWebApp(dto));
//	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Sitting> getSittingById(@PathVariable("id") UUID id) {
		return ResponseEntity.ok(sittingService.getById(id));
	}
	
	
	@PostMapping
	public ResponseEntity<Sitting> createSitting(SittingDto dto) {
		log.info("Http Handling createSitting, with userId :{}", dto.getUserId());
		return ResponseEntity.ok(sittingService.createSitting(dto));
	}
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@PostMapping("/test")
	public ResponseEntity<Sitting> createSittingTest(@RequestBody SittingDto dto) {
		log.info("Http Handling createSitting, with userId :{}, be :{}, end : {}", dto.getUserId(), dto.getShiftBeggining(), dto.getShiftEnd());
		return ResponseEntity.ok(sittingService.createSitting(dto));
	}
	
	@GetMapping("/postcodetest")
	public ResponseEntity<List<SittingToShowDto>> testMethode(String postcode) {
		return ResponseEntity.ok(sittingRepo.findAllByPostcode(postcode));
	}
	
	@GetMapping("/postcodetestpaginated")
	public ResponseEntity<List<SittingToShowDto>> testMethodePaginated(@RequestParam("postcode")String postcode, @RequestParam("page") int page) {
		Pageable firstPageWithTwoElements = PageRequest.of(page, 4);
		return ResponseEntity.ok(sittingRepo.findAllByPostcodePaginated(postcode, firstPageWithTwoElements));
	}
	

	@PostMapping("/testdatshit")
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

	@GetMapping("/getDispos")
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

//	@PostMapping("/search")
//	public List<User> search(@RequestBody DisponibilityDTO dispo) {
//		log.info("Http Handling Search with param : {}", dispo);
//		List<User> response = sittingService.search(dispo);
//		return response;
//	}
//
//	
//
//	@PostMapping("/add-dispo/{id}")
//	public void addDisponibilityToUser(@RequestBody DisponibilityDTO dispo, @PathVariable("id") String id) {
//
//		Disponibility d = createDispoFromDto(dispo);
//		User u = userRepo.findById(UUID.fromString(id)).get();
//		u.getDisponibility().add(d);
//		userRepo.save(u);
//		log.info("User u = {}, Dispo d = {}, d set to u", u, d);
//	}

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

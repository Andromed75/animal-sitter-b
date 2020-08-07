package com.example.animalsitter.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.example.animalsitter.domain.Sitting;
import com.example.animalsitter.domain.User;
import com.example.animalsitter.dto.MsPhotoDto;
import com.example.animalsitter.dto.PositonStackApiWrapperDto;
import com.example.animalsitter.dto.SittingDto;
import com.example.animalsitter.dto.SittingToShowDto;
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
	UserRepository userRepo;
	
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
	
	@GetMapping("/all")
	public ResponseEntity<List<SittingToShowDto>> getSittingsForwebApp(@RequestParam("postcode")String postcode, @RequestParam("page") int page) {
		log.info("Http handling getSittingsForwebApp");
		return ResponseEntity.ok(sittingService.findAllByPostcodePaginated(postcode, page));
	}
	
	@GetMapping("/search")
	public ResponseEntity<List<SittingToShowDto>> getSittingsForwebAppWithDates(@RequestParam("postcode")String postcode, @RequestParam("page") int page,
			@RequestParam("beg") String beg, @RequestParam("end") String end) {
		log.info("Http handling getSittingsForwebApp");
		return ResponseEntity.ok(sittingService.findAllByPostcodePaginatedWithDates(postcode, page, beg, end));
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

	@GetMapping("/find-all-users")
	public ResponseEntity<List<User>> getAllUsers() {
		return ResponseEntity.ok(userRepo.findAll());
	}
	
	@GetMapping("/phototest")
	public void testAppelMsPhoto() {
		RestTemplate rt = new RestTemplate();
		
	    String url = "http://localhost:9000/api/v1/photo/get/956599.jpg";
		log.debug("url to get picking tool {}",url);
		ResponseEntity<MsPhotoDto> response = rt.getForEntity(url , MsPhotoDto.class);
		if(HttpStatus.OK.equals(response.getStatusCode())) {
			MsPhotoDto dto = response.getBody();
			//pickingApp = (String) getPickingApp.getBody().get("pickingApp");
			log.info("HHTP OK RESULT, name {}, type {}, id {}", dto.getName(), dto.getType(), dto.getId());
		}else {
			log.info("error while calling store default picking app for store {} is {}");
		}
	}
	@GetMapping("/positionstack")
	public void testAppelPositionStack(@RequestParam("query") String address, @RequestParam("region") String city) {
		RestTemplate rt = new RestTemplate();
		
		String url = "http://api.positionstack.com/v1/forward?access_key=aac75ecebc69346fcfa68a5bbf5394d2&query="+address+"&region="+city+"&country=FR&limit=5&output=json&fields=results.longitude,results.latitude";
		log.debug("url to get picking tool {}",url);
		ResponseEntity<PositonStackApiWrapperDto> response = rt.getForEntity(url , PositonStackApiWrapperDto.class);
		if(HttpStatus.OK.equals(response.getStatusCode())) {
			PositonStackApiWrapperDto dto = response.getBody();
			//pickingApp = (String) getPickingApp.getBody().get("pickingApp");
			log.info("HHTP OK RESULT, name {}, type {}, id {}", dto.getData().stream().findFirst().get().getLatitude());
		}else {
			log.info("error while calling store default picking app for store {} is {}");
		}
	}

}

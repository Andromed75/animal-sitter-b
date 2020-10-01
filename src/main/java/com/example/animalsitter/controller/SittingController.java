package com.example.animalsitter.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
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
import com.example.animalsitter.repository.UserRepository;
import com.example.animalsitter.service.SittingService;

import lombok.extern.slf4j.Slf4j;

@RestController
@CrossOrigin
@RequestMapping("api/sitting/v1")
@Slf4j
public class SittingController {

	UserRepository userRepo;
	
	SittingService sittingService;

	@Autowired
	public SittingController(UserRepository userRepo, SittingService sittingService) {
		this.userRepo = userRepo;
		this.sittingService = sittingService;
	}
	
	/**
	 * @param postcode
	 * @param page
	 * @param beg
	 * @param end
	 * @return a paginated list off {@link SittingToShowDto}
	 */
	@GetMapping("/search")
	public ResponseEntity<List<SittingToShowDto>> getSittingsForwebAppWithDates(@RequestParam("postcode")String postcode, @RequestParam("page") int page,
			@RequestParam("beg") String beg, @RequestParam("end") String end) {
		log.info("Http handling getSittingsForwebApp");
		return ResponseEntity.ok(sittingService.findAllByPostcodePaginatedWithDates(postcode, page, beg, end));
	}
	
	@GetMapping("/search-mobile")
	public ResponseEntity<List<SittingToShowDto>> getSittingsForMobileAppWithDates(@RequestParam("postcode")String postcode,
			@RequestParam("beg") String beg, @RequestParam("end") String end) {
		log.info("Http handling getSittingsForwebApp");
		return ResponseEntity.ok(sittingService.findAllByPostcodeWithDates(postcode, beg, end));
	}
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@GetMapping("/{id}")
	public ResponseEntity<SittingToShowDto> getSittingById(@PathVariable("id") UUID id) {
		return ResponseEntity.ok(sittingService.getById(id));
	}
	
	@DeleteMapping("/{id}")
	public void deleteSitting(@PathVariable("id") UUID id) {
		log.info("Http Handling deleteSitting : {}", id);
		sittingService.deleteSitting(id);
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
	public ResponseEntity<List<SittingToShowDto>> getSittingsForwebApp() {
		log.info("Http handling getSittingsForwebApp");
		return ResponseEntity.ok(sittingService.findAllByPostcodePaginated("94", 0));
	}
	

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

	@GetMapping("/user-sittings/{id}")
	public ResponseEntity<List<Sitting>> getUserSittings(@PathVariable("id") UUID id) {
		log.info("Http Handling getUserSittings with user id : {}", id);
		return ResponseEntity.ok(sittingService.getUserSittings(id));
	}
	
}

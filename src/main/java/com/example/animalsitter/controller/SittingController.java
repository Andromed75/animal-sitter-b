package com.example.animalsitter.controller;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.animalsitter.domain.Disponibility;
import com.example.animalsitter.dto.DisponibilityDTO;
import com.example.animalsitter.repository.DisponibilityRepository;

import lombok.extern.slf4j.Slf4j;

@RestController
@CrossOrigin
@RequestMapping("api/sitting/v1")
@Slf4j 
public class SittingController {
	
	@Autowired
	DisponibilityRepository dispoRepo;

	@PostMapping
	public void testOffset(@RequestBody DisponibilityDTO dispo) {
		// OffsetDateTime ldt = OffsetDateTime.of(odd.getYear(), odd.getMonth(), odd.getDayOfMonth(), 0, 0, 0, 0, zos);
		DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		// DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SS", Locale.FRANCE);
		// OffsetDateTime date =  OffsetDateTime.parse(odd.getLocal(), inputFormatter , ZoneId.);
		OffsetDateTime beg = LocalDateTime.parse(dispo.getBeggining(), inputFormatter).atZone(ZoneId.of("Europe/Paris")).toOffsetDateTime();
		OffsetDateTime end = LocalDateTime.parse(dispo.getEnd(), inputFormatter).atZone(ZoneId.of("Europe/Paris")).toOffsetDateTime();
		log.info("beg = {}, end = {}", beg, end);
		Disponibility d = Disponibility.of(dispo);
		log.info("Dispo construit = {}", d);
		dispoRepo.save(d);
	}
	
	@GetMapping
	public ResponseEntity<List<Disponibility>> getAllDispo() {
		return ResponseEntity.ok(dispoRepo.findAll());
	}
	
	@GetMapping("/test-date/{id}")
	public boolean testDeGrandeur(@PathVariable("id") UUID id) {
		Disponibility d = dispoRepo.findById(id).get();
		boolean x = false;
		
		if(d.getShiftBeggining().compareTo(d.getShiftEnd()) > 0) {
			x = true;
			log.info("made true");
		}
		if(d.getShiftBeggining().compareTo(d.getShiftEnd()) < 0) {
			x = false;
			log.info("made false");
		}
		
		return x;
		
	}
	
}

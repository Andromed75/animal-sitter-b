package com.example.animalsitter.controller;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Locale;

import org.springframework.format.datetime.standard.TemporalAccessorParser;
import org.springframework.format.datetime.standard.TemporalAccessorPrinter;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.animalsitter.dto.OffsetDateDTO;

import lombok.extern.slf4j.Slf4j;

@RestController
@CrossOrigin
@RequestMapping("api/sitting/v1")
@Slf4j 
public class SittingController {

	@PostMapping
	public void testOffset(@RequestBody OffsetDateDTO odd) {
		log.info("DTO", odd.getDayOfMonth());
		// OffsetDateTime ldt = OffsetDateTime.of(odd.getYear(), odd.getMonth(), odd.getDayOfMonth(), 0, 0, 0, 0, zos);
		DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		// DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SS", Locale.FRANCE);
		// OffsetDateTime date =  OffsetDateTime.parse(odd.getLocal(), inputFormatter , ZoneId.);
		OffsetDateTime date = LocalDateTime.parse(odd.getLocal(), inputFormatter).atZone(ZoneId.of("Europe/Paris")).toOffsetDateTime();
		log.info("OffsetDateTime created = {}", date);
	}
	
}

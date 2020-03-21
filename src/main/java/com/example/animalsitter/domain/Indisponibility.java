package com.example.animalsitter.domain;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.example.animalsitter.dto.DisponibilityDTO;
import com.example.animalsitter.dto.IndispoDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Indisponibility {

	@Id
	@GeneratedValue
	UUID id;
	
	OffsetDateTime shiftBeggining;
	
	OffsetDateTime shiftEnd;
	
	public static Indisponibility of(DisponibilityDTO dispo) {
		DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		OffsetDateTime beg = LocalDateTime.parse(dispo.getBeggining(), inputFormatter).atZone(ZoneId.of("Europe/Paris")).toOffsetDateTime();
		OffsetDateTime end = LocalDateTime.parse(dispo.getEnd(), inputFormatter).atZone(ZoneId.of("Europe/Paris")).toOffsetDateTime();
		return Indisponibility.builder().shiftBeggining(beg).shiftEnd(end).build();
	}
	
}

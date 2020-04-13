package com.example.animalsitter.domain;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.example.animalsitter.dto.DisponibilityDTO;
import com.example.animalsitter.dto.request.StartAnEndWithUserId;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Disponibility {

	@Id
	@GeneratedValue
	UUID id;
	
	OffsetDateTime shiftBeggining;
	
	OffsetDateTime shiftEnd;
	
	@OneToMany(targetEntity = Indisponibility.class, cascade = CascadeType.ALL)
	List<Indisponibility> indisponibility;
	
	
	public static Disponibility of(DisponibilityDTO dispo) {
		DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		OffsetDateTime beg = LocalDateTime.parse(dispo.getBeggining(), inputFormatter).atZone(ZoneId.of("Europe/Paris")).toOffsetDateTime();
		OffsetDateTime end = LocalDateTime.parse(dispo.getEnd(), inputFormatter).atZone(ZoneId.of("Europe/Paris")).toOffsetDateTime();
		Disponibility d = new Disponibility(null, beg, end, new ArrayList<Indisponibility>());
		return d;
	}
	
	public static Disponibility of(StartAnEndWithUserId dispo) {
		DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		OffsetDateTime beg = LocalDateTime.parse(dispo.getDispoStart(), inputFormatter).atZone(ZoneId.of("Europe/Paris")).toOffsetDateTime();
		OffsetDateTime end = LocalDateTime.parse(dispo.getDispoEnd(), inputFormatter).atZone(ZoneId.of("Europe/Paris")).toOffsetDateTime();
		return new Disponibility(null, beg, end, new ArrayList<Indisponibility>());
	}
	
}

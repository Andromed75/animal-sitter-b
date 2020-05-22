package com.example.animalsitter.service;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.type.descriptor.java.OffsetDateTimeJavaDescriptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.animalsitter.domain.Disponibility;
import com.example.animalsitter.domain.Indisponibility;
import com.example.animalsitter.domain.User;
import com.example.animalsitter.dto.DisponibilityDTO;
import com.example.animalsitter.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SittingService {

	@Autowired
	UserRepository userRepository;
	
	public static void main(String[] args) {
		ZoneOffset zos = ZoneOffset.ofHours(1);
		OffsetDateTime ldt = OffsetDateTime.of(2020, 2, 3, 0, 0, 0, 0, zos);
		System.out.println(ldt.toString());
	}
	
	public List<User> search(DisponibilityDTO dispo) {
		DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		OffsetDateTime start = LocalDateTime.parse(dispo.getBeggining(), inputFormatter)
				.atZone(ZoneId.of("Europe/Paris")).plusHours(1).toOffsetDateTime();
		OffsetDateTime end = LocalDateTime.parse(dispo.getEnd(), inputFormatter).atZone(ZoneId.of("Europe/Paris"))
				.plusHours(1).toOffsetDateTime();
		List<User> allUsers = userRepository.findAll();
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
//		.stream().filter(i -> i.getShiftBeggining().compareTo(start) < 0 && i.getShiftEnd().compareTo(end) > 0).forEach(i -> response.add(u))));
		return response;
	}
}

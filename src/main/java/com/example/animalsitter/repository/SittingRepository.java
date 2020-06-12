package com.example.animalsitter.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.animalsitter.domain.Sitting;
import com.example.animalsitter.dto.SittingToShowDto;

public interface SittingRepository extends JpaRepository<Sitting, UUID> {

	
	@Query("SELECT new com.example.animalsitter.dto.SittingToShowDto(s.title, s.description, an,"
			+ " s.shiftBeggining, s.shiftEnd, u.firstName, u.lastName, u.age, u.phone, u.email, a.city, a.street, a.longitude, a.latitude)"
			+ "FROM Sitting s "
			+ "INNER JOIN User u ON u.id=s.userId "
			+ "INNER JOIN Animal an ON an.id=s.animal "
			+ "INNER JOIN Address a ON u.adress=a.id where a.postalcode = ?1")
	List<SittingToShowDto> findAllByPostcode(String postcode);
	
	@Query("SELECT new com.example.animalsitter.dto.SittingToShowDto(s.title, s.description, an,"
			+ " s.shiftBeggining, s.shiftEnd, u.firstName, u.lastName, u.age, u.phone, u.email, a.city, a.street, a.longitude, a.latitude)"
			+ "FROM Sitting s "
			+ "INNER JOIN User u ON u.id=s.userId "
			+ "INNER JOIN Animal an ON an.id=s.animal "
			+ "INNER JOIN Address a ON u.adress=a.id where a.postalcode = ?1")
	List<SittingToShowDto> findAllByPostcodePaginated(String postcode, Pageable pageable);
	
}

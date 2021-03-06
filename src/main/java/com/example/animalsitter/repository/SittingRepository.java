package com.example.animalsitter.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.animalsitter.domain.Sitting;
import com.example.animalsitter.dto.SittingToShowDto;

public interface SittingRepository extends JpaRepository<Sitting, UUID> {

	
//	@Query("SELECT new com.example.animalsitter.dto.SittingToShowDto(s.title, s.description, an,"
//			+ " s.shiftBeggining, s.shiftEnd, u.firstName, u.lastName, u.age, u.phone, u.email, a.city, a.street, a.longitude, a.latitude)"
//			+ "FROM Sitting s "
//			+ "INNER JOIN User u ON u.id=s.userId "
//			+ "INNER JOIN Animal an ON an.id=s.animal "
//			+ "INNER JOIN Address a ON u.adress=a.id where a.postalcode = ?1")
//	List<SittingToShowDto> findAllByPostcode(String postcode);
	
	@Query("SELECT new com.example.animalsitter.dto.SittingToShowDto(s.id, s.title, s.description, an,"
			+ " s.shiftBeggining, s.shiftEnd, u.firstName, u.lastName, u.age, u.phone, u.email, a.city, a.street, a.longitude, a.latitude)"
			+ "FROM Sitting s "
			+ "INNER JOIN User u ON u.id=s.userId "
			+ "INNER JOIN Animal an ON an.id=s.animal "
			+ "INNER JOIN Address a ON u.adress=a.id "
			+ "WHERE a.postalcode like ?1%")
	List<SittingToShowDto> findAllByPostcodePaginated(String postcode, Pageable pageable);
	
	@Query("SELECT new com.example.animalsitter.dto.SittingToShowDto(s.id, s.title, s.description, an,"
			+ " s.shiftBeggining, s.shiftEnd, u.firstName,"
			+ "u.lastName, u.age, u.phone, u.email, a.city, a.street, a.longitude, a.latitude)"
			+ "FROM Sitting s "
			+ "INNER JOIN User u ON u.id=s.userId "
			+ "INNER JOIN Animal an ON an.id=s.animal "
			+ "INNER JOIN Address a ON u.adress=a.id "
			+ "WHERE a.postalcode like (?1%) and (?2) <= s.shiftBeggining and (?3) >= s.shiftEnd")
	List<SittingToShowDto> findAllByPostcodePaginatedWithDate(
			String postcode,LocalDateTime beg,LocalDateTime end, Pageable pageable);

	@Query("SELECT new com.example.animalsitter.dto.SittingToShowDto(s.id, s.title, s.description, an,"
			+ " s.shiftBeggining, s.shiftEnd, u.firstName,"
			+ "u.lastName, u.age, u.phone, u.email, a.city, a.street, a.longitude, a.latitude)"
			+ "FROM Sitting s "
			+ "INNER JOIN User u ON u.id=s.userId "
			+ "INNER JOIN Animal an ON an.id=s.animal "
			+ "INNER JOIN Address a ON u.adress=a.id "
			+ "WHERE s.id = (?1)")
	SittingToShowDto findSittingById(UUID id);

	
	@Query("SELECT new com.example.animalsitter.dto.SittingToShowDto(s.id, s.title, s.description, an,"
			+ " s.shiftBeggining, s.shiftEnd, u.firstName,"
			+ "u.lastName, u.age, u.phone, u.email, a.city, a.street, a.longitude, a.latitude)"
			+ "FROM Sitting s "
			+ "INNER JOIN User u ON u.id=s.userId "
			+ "INNER JOIN Animal an ON an.id=s.animal "
			+ "INNER JOIN Address a ON u.adress=a.id "
			+ "WHERE a.postalcode like (?1%) and (?2) <= s.shiftBeggining and (?3) >= s.shiftEnd")
	List<SittingToShowDto> findAllByPostcodeWithDate(String postcode, LocalDateTime beg, LocalDateTime end);
}

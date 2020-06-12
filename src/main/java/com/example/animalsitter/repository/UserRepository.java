package com.example.animalsitter.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.animalsitter.domain.User;

public interface UserRepository extends JpaRepository<User, UUID>{
	
	Optional<User> findByPseudo(String pseudo);
	
	Boolean existsByPseudo(String pseudo);
	
	Boolean existsByEmail(String email);


	
	@Query("SELECT u FROM User u INNER JOIN Address a ON u.adress=a.id where a.postalcode = ?1")
	List<User> findAllByPostalcode(String postcode);
}

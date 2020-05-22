package com.example.animalsitter.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.animalsitter.domain.User;

public interface UserRepository extends JpaRepository<User, UUID>{
	
	Optional<User> findByPseudo(String pseudo);
	
	Boolean existsByPseudo(String pseudo);
	
	Boolean existsByEmail(String email);
}

package com.example.animalsitter.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.animalsitter.domain.Disponibility;

public interface DisponibilityRepository extends JpaRepository<Disponibility, UUID> {

	
	
}

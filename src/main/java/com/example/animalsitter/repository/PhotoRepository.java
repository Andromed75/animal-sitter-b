package com.example.animalsitter.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.animalsitter.domain.Photo;

public interface PhotoRepository extends JpaRepository<Photo, UUID>{
	
	
}

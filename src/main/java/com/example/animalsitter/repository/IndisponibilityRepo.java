package com.example.animalsitter.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.animalsitter.domain.Indisponibility;

public interface IndisponibilityRepo extends JpaRepository<Indisponibility, UUID>{

}

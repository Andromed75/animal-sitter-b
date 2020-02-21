package com.example.animalsitter.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.animalsitter.domain.Sickness;

public interface SicknessRepository extends JpaRepository<Sickness, UUID>{

}

package com.example.animalsitter.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.animalsitter.domain.Animal;

public interface Animalrepository extends JpaRepository<Animal, UUID>{

}

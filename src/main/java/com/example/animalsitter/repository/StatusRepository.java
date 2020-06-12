package com.example.animalsitter.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.animalsitter.domain.Status;


public interface StatusRepository extends JpaRepository<Status, UUID>{

}

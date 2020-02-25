package com.example.animalsitter.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.animalsitter.domain.Admin;

public interface AdminRepository extends JpaRepository<Admin, UUID>{

}

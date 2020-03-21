package com.example.animalsitter.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.animalsitter.domain.Role;
import com.example.animalsitter.enums.ERole;

public interface RoleRepository extends JpaRepository<Role, UUID>{
	Optional<Role> findByName(ERole name);

}

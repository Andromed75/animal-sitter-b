package com.example.animalsitter.domain;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.example.animalsitter.enums.StatusEnum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Status {

	@Id
	@GeneratedValue
	UUID id;
	
	StatusEnum status;
	
	LocalDateTime date;
}

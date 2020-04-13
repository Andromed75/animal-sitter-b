package com.example.animalsitter.dto.request;

import java.util.UUID;

import lombok.Data;

@Data
public class StartAnEndWithUserId {

	UUID id;
	String dispoStart;
	String dispoEnd;
	
}

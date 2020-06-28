package com.example.animalsitter.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MsPhotoDto {
		
	UUID id;
	
	private String name;
	
	private String type;
	
	private byte[] image;
	
}

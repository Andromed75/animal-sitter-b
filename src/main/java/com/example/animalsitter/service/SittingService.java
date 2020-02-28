package com.example.animalsitter.service;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import org.hibernate.type.descriptor.java.OffsetDateTimeJavaDescriptor;

public class SittingService {

	
	public static void main(String[] args) {
		ZoneOffset zos = ZoneOffset.ofHours(1);
		OffsetDateTime ldt = OffsetDateTime.of(2020, 2, 3, 0, 0, 0, 0, zos);
		System.out.println(ldt.toString());
	}
}

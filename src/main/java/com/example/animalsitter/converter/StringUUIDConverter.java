package com.example.animalsitter.converter;

import java.util.UUID;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = false)
public class StringUUIDConverter implements AttributeConverter<UUID, String>{

	@Override
	public String convertToDatabaseColumn(UUID attribute) {
		return attribute.toString();
	}

	@Override
	public UUID convertToEntityAttribute(String dbData) {
		return UUID.fromString(dbData);
	}

}

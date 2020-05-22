package com.example.animalsitter.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CannotCreateException extends RuntimeException{

	String message;
	
	
}
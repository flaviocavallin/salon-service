package com.example.salon.exceptions;

public class EntityNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 5876000884725488713L;

	public EntityNotFoundException(String message) {
        super(message);
    }
}
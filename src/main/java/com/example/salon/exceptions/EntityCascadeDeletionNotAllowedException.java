package com.example.salon.exceptions;

public class EntityCascadeDeletionNotAllowedException extends RuntimeException {

	private static final long serialVersionUID = -6266533579718258033L;

	public EntityCascadeDeletionNotAllowedException(String message) {
        super(message);
    }
}
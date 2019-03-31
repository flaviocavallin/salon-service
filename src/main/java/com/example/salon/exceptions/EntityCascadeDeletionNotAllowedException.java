package com.example.salon.exceptions;

public class EntityCascadeDeletionNotAllowedException extends RuntimeException {

    public EntityCascadeDeletionNotAllowedException(String message) {
        super(message);
    }
}
package com.example.salon.exceptions.handler;

import com.example.salon.exceptions.EntityCascadeDeletionNotAllowedException;
import com.example.salon.exceptions.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

/**
 * Custom exception handler
 */
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
class RestControllerExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestControllerExceptionHandler.class);

    @ExceptionHandler({EntityNotFoundException.class})
    public ResponseEntity<ApiError> notFound(RuntimeException ex) {

        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, new Date(), ex.getMessage());

        LOGGER.debug("Exception handled={}", apiError);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
    }

    @ExceptionHandler({EntityCascadeDeletionNotAllowedException.class})
    public ResponseEntity<ApiError> notAllowed(EntityCascadeDeletionNotAllowedException ex) {

        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, new Date(), ex.getMessage());

        LOGGER.debug("Exception handled={}", apiError);

        return ResponseEntity.badRequest().body(apiError);
    }
}
package com.example.salon.exceptions.handler;

import com.example.salon.exceptions.EntityCascadeDeletionNotAllowedException;
import com.example.salon.exceptions.EntityNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class RestControllerExceptionHandlerTest {

    private static final String ERROR_MESSAGE = "ClientId not found";

    @Test
    public void restControllerExceptionHandlerTest() {
        RestControllerExceptionHandler handler = new RestControllerExceptionHandler();

        ResponseEntity<ApiError> responseEntity = handler.notFound(new EntityNotFoundException(ERROR_MESSAGE));

        Assertions.assertThat(responseEntity.getBody()).isNotNull();
        Assertions.assertThat(responseEntity.getBody().getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
        Assertions.assertThat(responseEntity.getBody().getMessage()).isEqualTo(ERROR_MESSAGE);
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }


    @Test
    public void notAllowedExceptionHandlerTest() {
        RestControllerExceptionHandler handler = new RestControllerExceptionHandler();

        ResponseEntity<ApiError> responseEntity = handler.notAllowed(new EntityCascadeDeletionNotAllowedException(ERROR_MESSAGE));

        Assertions.assertThat(responseEntity.getBody()).isNotNull();
        Assertions.assertThat(responseEntity.getBody().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
        Assertions.assertThat(responseEntity.getBody().getMessage()).isEqualTo(ERROR_MESSAGE);
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}

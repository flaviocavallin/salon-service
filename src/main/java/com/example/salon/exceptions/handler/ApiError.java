package com.example.salon.exceptions.handler;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.http.HttpStatus;

import java.util.Date;
import java.util.Objects;

public class ApiError {

    private HttpStatus status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private Date date;
    private String message;

    ApiError(HttpStatus status, Date date, String message) {
        this.status = Objects.requireNonNull(status, "status can not be null");
        this.date = Objects.requireNonNull(date, "date can not be null");
        this.message = Objects.requireNonNull(message, "message can not be null");
    }

    public HttpStatus getStatus() {
        return status;
    }

    public Date getDate() {
        return date;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}

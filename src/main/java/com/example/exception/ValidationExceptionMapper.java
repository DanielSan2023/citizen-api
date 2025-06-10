package com.example.exception;

import jakarta.validation.ValidationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.time.Instant;

@Provider
public class ValidationExceptionMapper implements ExceptionMapper<ValidationException> {

    @Override
    public Response toResponse(ValidationException exception) {
        ApiError apiError = new ApiError(
                Response.Status.BAD_REQUEST.getStatusCode(),
                "Validation Error",
                exception.getMessage(),
                null,
                Instant.now()
        );
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(apiError)
                .type("application/xml")
                .build();
    }
}

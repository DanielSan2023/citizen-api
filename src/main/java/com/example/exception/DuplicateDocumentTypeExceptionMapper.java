package com.example.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.time.Instant;

@Provider
public class DuplicateDocumentTypeExceptionMapper implements ExceptionMapper<DuplicateDocumentTypeException> {
    @Override
    public Response toResponse(DuplicateDocumentTypeException e) {
        ApiError apiError = new ApiError(
                Response.Status.BAD_REQUEST.getStatusCode(),
                "Duplicate Document Type",
                e.getMessage(),
                null,
                Instant.now()
        );

        return Response.status(Response.Status.BAD_REQUEST)
                .entity(apiError)
                .type("application/xml")
                .build();
    }
}

package com.example.exception;

public class DuplicateDocumentTypeException extends RuntimeException {
    public DuplicateDocumentTypeException(String message) {
        super(message);
    }

    public DuplicateDocumentTypeException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateDocumentTypeException(Throwable cause) {
        super(cause);
    }
}
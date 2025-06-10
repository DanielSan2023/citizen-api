package com.example.exception;

import jakarta.xml.bind.annotation.XmlRootElement;

import java.time.Instant;

@XmlRootElement(name = "error")
public class ApiError {
    private int status;
    private String error;
    private String message;
    private String details;
    private Instant timestamp;

    public ApiError() {
    }

    public ApiError(int status, String error, String message, String details, Instant timestamp) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.details = details;
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }
}
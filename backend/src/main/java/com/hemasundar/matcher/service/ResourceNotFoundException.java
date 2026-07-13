package com.hemasundar.matcher.service;

/** Thrown when a requested document or match report doesn't exist. */
public class ResourceNotFoundException extends DocumentProcessingException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
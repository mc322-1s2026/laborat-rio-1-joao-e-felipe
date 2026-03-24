package com.nexus.exception;

public class NexusValidationException extends RuntimeException {
    private static int totalValidationErrors = 0;

    public NexusValidationException(String message) {
        super(message);
        totalValidationErrors++;
    }

    // Getters
    public static int getTotalValidationErrors() { return totalValidationErrors; }
}
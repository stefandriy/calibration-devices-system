package com.softserve.edu.service.exceptions;

public class NotAvailableException extends RuntimeException {
    public NotAvailableException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotAvailableException(String message) {
        super(message);
    }

    public NotAvailableException() {
    }
}
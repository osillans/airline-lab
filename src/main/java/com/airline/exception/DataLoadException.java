package com.airline.exception;

public class DataLoadException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public DataLoadException(String message) {
        super(message);
    }

    public DataLoadException(String message, Throwable cause) {
        super(message, cause);
    }
}
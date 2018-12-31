package com.tablefootbal.server.exceptions.customExceptions;

public class InvalidJsonException extends RuntimeException {
    public InvalidJsonException(String message) {
        super(message);
    }
}

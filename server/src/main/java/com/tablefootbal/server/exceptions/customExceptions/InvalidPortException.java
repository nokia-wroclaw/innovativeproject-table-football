package com.tablefootbal.server.exceptions.customExceptions;

public class InvalidPortException extends RuntimeException {
    public InvalidPortException() {
    }

    public InvalidPortException(String message) {
        super(message);
    }
}

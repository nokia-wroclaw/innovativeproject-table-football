package com.tablefootbal.server.exceptions;

import com.tablefootbal.server.exceptions.customExceptions.InvalidPortException;
import com.tablefootbal.server.exceptions.customExceptions.SensorNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidPortException.class)
    public ResponseEntity<String> handleInvalidPort(InvalidPortException ex) {

        return new ResponseEntity<>(ex.getMessage(), HttpStatus.FORBIDDEN);
    }
    
    @ExceptionHandler(SensorNotFoundException.class)
    public ResponseEntity<String> handleSensorNotFound(InvalidPortException ex) {
        
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.FORBIDDEN);
    }
}

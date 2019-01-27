package com.tablefootbal.server.notifications.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidRegisterDataException extends RuntimeException {

    public InvalidRegisterDataException(){ super();}

    public InvalidRegisterDataException(String msg){
        super(msg);
    }

}

package com.tablefootbal.server.exceptions.customExceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidSensorDataException extends RuntimeException
{
	public InvalidSensorDataException()
	{
		super();
	}
	
	public InvalidSensorDataException(String message)
	{
		super(message);
	}
}

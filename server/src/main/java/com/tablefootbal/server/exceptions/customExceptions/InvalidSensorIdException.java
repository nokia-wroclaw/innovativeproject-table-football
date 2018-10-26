package com.tablefootbal.server.exceptions.customExceptions;

public class InvalidSensorIdException extends RuntimeException
{
	public InvalidSensorIdException()
	{
	}
	
	public InvalidSensorIdException(String message)
	{
		super(message);
	}
}

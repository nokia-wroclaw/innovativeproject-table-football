package com.tablefootbal.server.exceptions.customExceptions;

public class SensorNotFoundException extends RuntimeException
{
	public SensorNotFoundException(String message)
	{
		super(message);
	}
}

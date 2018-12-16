package com.tablefootbal.server.events;

import com.tablefootbal.server.readings.SensorReadings;
import org.springframework.context.ApplicationEvent;

public class SensorUpdateEvent extends ApplicationEvent
{
	private SensorReadings.Reading reading;
	
	public SensorUpdateEvent(Object source, SensorReadings.Reading reading)
	{
		super(source);
		this.reading = reading;
	}
	
public 	SensorReadings.Reading getReading()
	{
		return reading;
	}
}

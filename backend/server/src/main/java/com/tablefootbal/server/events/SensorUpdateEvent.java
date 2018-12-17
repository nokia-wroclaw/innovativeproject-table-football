package com.tablefootbal.server.events;

import com.tablefootbal.server.dto.ReadingDto;
import org.springframework.context.ApplicationEvent;

public class SensorUpdateEvent extends ApplicationEvent
{
	private ReadingDto reading;
	
	public SensorUpdateEvent(Object source, ReadingDto reading)
	{
		super(source);
		this.reading = reading;
	}
	
public 	ReadingDto getReading()
	{
		return reading;
	}
}

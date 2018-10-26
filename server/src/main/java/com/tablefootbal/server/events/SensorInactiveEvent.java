package com.tablefootbal.server.events;

import org.springframework.context.ApplicationEvent;

public class SensorInactiveEvent extends ApplicationEvent
{
	SensorInactiveEvent(Object sensorId)
	{
		super(sensorId);
	}
	
	@Override
	public Object getSource()
	{
		return super.getSource();
	}
}

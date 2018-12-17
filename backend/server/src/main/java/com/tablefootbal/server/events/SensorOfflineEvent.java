package com.tablefootbal.server.events;

import org.springframework.context.ApplicationEvent;

public class SensorOfflineEvent extends ApplicationEvent
{
	SensorOfflineEvent(Object sensorId)
	{
		super(sensorId);
	}
	
	@Override
	public Object getSource()
	{
		return super.getSource();
	}
}

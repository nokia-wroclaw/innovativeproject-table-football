package com.tablefootbal.server.events;

import org.springframework.context.ApplicationEventPublisher;

public class HeartbeatTrackerTask implements Runnable
{
	private final ApplicationEventPublisher eventPublisher;
	
	private final String sensorId;
	
	 HeartbeatTrackerTask(ApplicationEventPublisher eventPublisher, String sensorId)
	{
		this.eventPublisher = eventPublisher;
		this.sensorId = sensorId;
	}
	
	@Override
	public void run()
	{
		SensorInactiveEvent event = new SensorInactiveEvent(sensorId);
		eventPublisher.publishEvent(event);
	}
}

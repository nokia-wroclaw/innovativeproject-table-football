package com.tablefootbal.server.events;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;

@Component
@PropertySource("classpath:readings.properties")
public class SensorTrackingScheduler implements ApplicationListener<SensorInactiveEvent>
{
	private static int ONE_SECOND_IN_MILIS = 1000;
	
	@Value("${readings.heartbeat_interval}")
	private int HEARTBEAT_INTERVAL;
	
	final private
	ApplicationEventPublisher eventPublisher;
	
	private Map<String, Future<?>> scheduledTasks;
	
	final private
	ThreadPoolTaskScheduler scheduler;
	
	@Autowired
	public SensorTrackingScheduler(ApplicationEventPublisher eventPublisher,
	                               ThreadPoolTaskScheduler threadPoolTaskScheduler)
	{
		this.scheduler = threadPoolTaskScheduler;
		this.scheduledTasks = new HashMap<>();
		this.eventPublisher = eventPublisher;
	}
	
	public void startTracking(String id)
	{
		Future task;
		if ((task = scheduledTasks.get(id)) != null)
		{
			task.cancel(true);
		}
		
		Runnable trackingTask = new HeartbeatTrackerTask(eventPublisher, id);
		
		long period = HEARTBEAT_INTERVAL * ONE_SECOND_IN_MILIS;
		
		ScheduledFuture scheduledTask = scheduler.scheduleAtFixedRate(trackingTask,
				new Date(System.currentTimeMillis() + period),
				period);
		
		scheduledTasks.put(id, scheduledTask);
	}
	
	@Override
	public void onApplicationEvent(SensorInactiveEvent sensorInactiveEvent)
	{
		String sensorId = (String) sensorInactiveEvent.getSource();
		Future scheduledTask = scheduledTasks.get(sensorId);
		scheduledTask.cancel(false);
		
		scheduledTasks.remove(sensorId);
	}
}

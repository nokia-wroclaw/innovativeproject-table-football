package com.tablefootbal.server.events;

import com.tablefootbal.server.entity.Sensor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
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
	@Value("${readings.heartbeat_interval}")
	private int HEARTBEAT_INTERVAL;
	
	final private
	ApplicationEventPublisher eventPublisher;
	
	private Map<String, Future<?>> scheduledTasks;
	
	final private
	ThreadPoolTaskScheduler scheduler;
	
	@Autowired
	public SensorTrackingScheduler(ApplicationEventPublisher eventPublisher)
	{
		this.scheduler = threadPoolTaskScheduler();
		this.scheduledTasks = new HashMap<>();
		this.eventPublisher = eventPublisher;
	}
	
	@Bean
	public ThreadPoolTaskScheduler threadPoolTaskScheduler()
	{
		return new ThreadPoolTaskScheduler();
	}
	
	public void startTracking(String id)
	{
		Future task;
		if ((task = scheduledTasks.get(id)) != null)
		{
			task.cancel(true);
		}
		
		Runnable trackingTask = new HeartbeatTrackerTask(eventPublisher, id);
		
		ScheduledFuture scheduledTask = scheduler.scheduleAtFixedRate(trackingTask,
				new Date(System.currentTimeMillis() + HEARTBEAT_INTERVAL),
				HEARTBEAT_INTERVAL);
		
		scheduledTasks.put(id, scheduledTask);
	}
	
	@Override
	public void onApplicationEvent(SensorInactiveEvent sensorInactiveEvent)
	{
		Sensor sensor = (Sensor) sensorInactiveEvent.getSource();
		Future scheduledTask = scheduledTasks.get(sensor.getId());
		scheduledTask.cancel(false);
		
		scheduledTasks.remove(sensor.getId());
	}
}

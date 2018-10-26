package com.tablefootbal.server;

import com.tablefootbal.server.entity.Sensor;
import com.tablefootbal.server.events.SensorDataManager;
import com.tablefootbal.server.events.SensorTrackingScheduler;
import com.tablefootbal.server.events.SensorUpdateEvent;
import com.tablefootbal.server.readings.SensorReadings;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;

import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@TestPropertySource("classpath:readings.properties")
public class SensorDataManagerIntegrationTests extends TestCase
{
	@Value("${readings.threshold}")
	private int THRESHOLD;
	
	@Value("${readings.max_readings}")
	private int MAX_READINGS;
	
	@InjectMocks
	SensorDataManager manager;
	
	@Mock
	SensorTrackingScheduler scheduler;
	
	private SensorUpdateEvent event;
	
	
	@Before
	public void init()
	{
		
		Sensor sensor = new Sensor();
		sensor.setId("11:11:11:11:11:11");
		sensor.setLastNotificationDate(new Date());
		sensor.setOnline(true);
		sensor.setActive(true);
		sensor.setFloor(1);
		sensor.setRoom(111);
		
		event = mock(SensorUpdateEvent.class);
		when(event.getSource()).thenReturn(sensor);
		
		doNothing().when(scheduler).startTracking(Mockito.anyString());
		
		ReflectionTestUtils.setField(manager, "THRESHOLD", THRESHOLD);
		ReflectionTestUtils.setField(manager, "MAX_READINGS", MAX_READINGS);
		
	}
	
	@Test
	public void givenAverageLowerThanThreshold_thenSensorInactive()
	{
		when(event.getReading()).thenReturn(
				new SensorReadings.Reading(0, 0, 0, System.currentTimeMillis()));
		
		manager.onApplicationEvent(event);
		
		Sensor sensor = (Sensor) event.getSource();
		
		Assert.assertFalse(sensor.isActive());
	}
	
	@Test
	public void givenUpdateEventPublished_thenTaskTrackerStarts()
	{
		when(event.getReading()).thenReturn(
				new SensorReadings.Reading(0, 0, 0, System.currentTimeMillis()));
		
		manager.onApplicationEvent(event);
		
		Mockito.verify(scheduler, times(1)).startTracking(anyString());
	}
	
	
}

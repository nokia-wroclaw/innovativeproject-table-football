package com.tablefootbal.server;


import com.tablefootbal.server.readings.SensorReadings;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource("classpath:readings.properties")
public class SensorReadingsTests
{
	@Value("${readings.max_readings}")
	int maxReadings;
	
	private SensorReadings sensorReadings;
	
	@Before
	public void init()
	{
		
		sensorReadings = new SensorReadings(maxReadings);
	}
	
	@Test
	public void givenReadingAdded_thenReadingPresentInTheList()
	{
		SensorReadings.Reading reading = new SensorReadings.Reading(10, 10, 10,
				System.currentTimeMillis());
		
		sensorReadings.addReading(reading);
		
		Assert.assertEquals(sensorReadings.getReadings().get(0).getX(), 10,0.1);
		Assert.assertEquals(sensorReadings.getReadings().get(0).getY(), 10,0.1);
		Assert.assertEquals(sensorReadings.getReadings().get(0).getZ(), 10,0.1);
	}
	
	@Test
	public void testAverageCalculation()
	{
		SensorReadings.Reading reading;
		for (int i = 0; i < 5; i++)
		{
			reading = new SensorReadings.Reading(10, 10, 10,
					System.currentTimeMillis());
			
			sensorReadings.addReading(reading);
		}
		
		Assert.assertEquals((3 * 10 * 5) / 5, sensorReadings.getAverage(),0.1);
	}
	
	@Test
	public void givenMoreThanSizeRecords_thenOldestOneIsRemoved()
	{
		SensorReadings.Reading reading;
		for (int i = 0; i < maxReadings + 1; i++)
		{
			reading = new SensorReadings.Reading(i, i, i,
					System.currentTimeMillis());
			sensorReadings.addReading(reading);
		}
		
		Assert.assertEquals(1, sensorReadings.getReadings().getFirst().getX(),0.1);
		Assert.assertEquals(maxReadings, sensorReadings.getReadings().getLast().getX(),0.1);
		Assert.assertEquals(maxReadings, sensorReadings.getReadings().size());
	}
}

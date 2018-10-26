package com.tablefootbal.server;

import com.tablefootbal.server.entity.Sensor;
import com.tablefootbal.server.exceptions.customExceptions.InvalidSensorIdException;
import com.tablefootbal.server.service.SensorService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SensorServiceTests
{
	@Autowired
	SensorService service;
	
	
	@Test(expected = InvalidSensorIdException.class)
	public void givenNullIdPassedToSave_thenExceptionThrown()
	{
		Sensor sensor = new Sensor();
		sensor.setId(null);
		service.saveOrUpdate(sensor);
	}
}

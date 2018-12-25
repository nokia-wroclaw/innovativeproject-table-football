package com.tablefootbal.server;

import com.tablefootbal.server.entity.Sensor;
import com.tablefootbal.server.repository.SensorRepository;
import com.tablefootbal.server.service.SensorService;
import com.tablefootbal.server.service.SensorServiceImp;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
public class SensorServiceTests
{
	
	@Mock
	SensorRepository repository;
	
	@InjectMocks
	SensorServiceImp service;
	
	@Before
	public void init()
	{
		//3 active sensors, 3 online sensors, 3 on floor 1, 2 in room 111, 1 in room 100,
		// 2 on floor 2, 1 in room 222, 1 in room 200
		Sensor sensor;
		List<Sensor> sensors = new ArrayList<>();
		
		sensor = new Sensor("11:11:11:11:11:11", true, false, new Date());
		sensor.setFloor(1);
		sensor.setRoom(111);
		sensors.add(sensor);
		
		sensor = new Sensor("22:22:22:22:22:22", true, true, new Date());
		sensor.setFloor(1);
		sensor.setRoom(111);
		sensors.add(sensor);
		
		sensor = new Sensor("33:33:33:33:33:33", false, true, new Date());
		sensor.setFloor(1);
		sensor.setRoom(100);
		sensors.add(sensor);
		
		sensor = new Sensor("44:44:44:44:44:44", false, false, new Date());
		sensor.setFloor(2);
		sensor.setRoom(222);
		sensors.add(sensor);
		
		sensor = new Sensor("55:55:55:55:55:55", true, true, new Date());
		sensor.setFloor(2);
		sensor.setRoom(200);
		sensors.add(sensor);
		
		Mockito.when(repository.findAll()).thenReturn(sensors);
	}
	
	@Test
	public void testFindAllInRoom()
	{
		List<Sensor> sensors = service.findAllInRoom(111);
		Assert.assertEquals(2,sensors.size());
		for (Sensor s: sensors)
		{
			Assert.assertEquals(111, s.getRoom());
		}
	}
	
	@Test
	public void testFindAllOnFloor()
	{
		List<Sensor> sensors = service.findAllOnFloor(1);
		Assert.assertEquals(3,sensors.size());
		for (Sensor s: sensors)
		{
			Assert.assertEquals(1, s.getFloor());
		}
	}
	
	@Test
	public void testFindAllOccupied()
	{
		List<Sensor> sensors = service.findOccupiedSensors();
		Assert.assertEquals(3,sensors.size());
		for (Sensor s: sensors)
		{
			Assert.assertTrue(s.isActive());
		}
	}
	
	@Test
	public void testFindAllFree()
	{
		List<Sensor> sensors = service.findFreeSensors();
		Assert.assertEquals(2,sensors.size());
		for (Sensor s: sensors)
		{
			Assert.assertTrue(!s.isActive());
		}
	}
	
}



































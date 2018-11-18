package com.tablefootbal.server;

import com.tablefootbal.server.entity.Sensor;
import com.tablefootbal.server.repository.SensorRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class SensorRepositoryIntegrationTests
{
	@Autowired
	public SensorRepository repository;
	
	@Test
	public void whenCountCalled_Then5Returned()
	{
		repository.count();
	}
	
	@Test
	public void whenQueriedForActive_Then2EntriesReturned()
	{
		List<Sensor> result = repository.findAllByActiveIsTrue();
		Assert.assertEquals(result.size(), 2);
		
		for (Sensor sensor : result)
		{
			Assert.assertTrue(sensor.isActive());
		}
	}
	
	@Test
	public void whenQueriedForOffline_Then2EntriesReturned()
	{
		List<Sensor> result = repository.findAllByOnlineIsFalse();
		Assert.assertEquals(result.size(), 2);
		
		for (Sensor sensor : result)
		{
			Assert.assertFalse(sensor.isOnline());
		}
	}
	
	@Test
	public void whenQueriedForOnline_Then4EntriesReturned()
	{
		List<Sensor> result = repository.findAllByOnlineIsTrue();
		Assert.assertEquals(result.size(), 4);
		
		for (Sensor sensor : result)
		{
			Assert.assertTrue(sensor.isOnline());
		}
	}
	
	@Test
	public void whenQueriedForFloor3_Then2EntriesReturned()
	{
		List<Sensor> result = repository.findAllByFloor(3);
		
		for (Sensor sensor : result)
		{
			Assert.assertEquals(sensor.getFloor(), 3);
		}
	}
	
	@Test
	public void whenQueriedForRoom222_Then2EntriesReturned()
	{
		List<Sensor> result = repository.findAllByRoom(222);
		
		for (Sensor sensor : result)
		{
			Assert.assertEquals(sensor.getRoom(), 222);
		}
	}
	
	@Test
	@Rollback
	public void testNewSensorSaved()
	{
		Sensor sensor = new Sensor();
		sensor.setId("FD:DD:84:21:C2:11");
		
		repository.save(sensor);
		
		Optional<Sensor> result = repository.findById(sensor.getId());
		
		Assert.assertEquals(sensor.getId(), result.get().getId());
		
	}
	
	
}

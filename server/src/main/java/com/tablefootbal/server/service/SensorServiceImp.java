package com.tablefootbal.server.service;

import com.tablefootbal.server.entity.Sensor;
import com.tablefootbal.server.exceptions.customExceptions.InvalidSensorIdException;
import com.tablefootbal.server.repository.SensorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class SensorServiceImp implements SensorService
{
	private final SensorRepository repository;
	
	@Autowired
	public SensorServiceImp(SensorRepository repository)
	{
		this.repository = repository;
	}
	
	@Override
	public Sensor saveOrUpdate(Sensor sensor)
	{
		String id = sensor.getId();
		if (id == null)
		{
			throw new InvalidSensorIdException();
		}
		
		if (!repository.existsById(sensor.getId()))
		{
			repository.save(sensor);
		}
		//TODO: Publish even containing current date to update status
		
		return sensor;
	}
	
	@Override
	public boolean checkIfExistsById(String id)
	{
		return repository.existsById(id);
	}
	
	@Override
	public List<Sensor> findActiveSensors()
	{
		return repository.findAllByActiveIsTrue();
	}
	
	@Override
	public List<Sensor> findUnactiveSensors()
	{
		return repository.findAllByActiveIsFalse();
	}
	
	@Override
	public List<Sensor> findConnectedSensors()
	{
		return null;
	}
	
	@Override
	public List<Sensor> findConnected()
	{
		return repository.findAllByOnlineIsTrue();
	}
	
	@Override
	public List<Sensor> findDisconnected()
	{
		return repository.findAllByOnlineIsFalse();
	}
	
	@Override
	public List<Sensor> findAllOnFloor(int floor)
	{
		return repository.findAllByFloor(floor);
	}
	
	@Override
	public List<Sensor> findAllInRoom(int room)
	{
		return repository.findAllByRoom(room);
	}
}

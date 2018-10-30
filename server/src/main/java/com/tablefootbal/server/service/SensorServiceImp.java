package com.tablefootbal.server.service;

import com.tablefootbal.server.entity.Sensor;
import com.tablefootbal.server.events.SensorInactiveEvent;
import com.tablefootbal.server.events.SensorUpdateEvent;
import com.tablefootbal.server.readings.SensorReadings;
import com.tablefootbal.server.repository.SensorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional
public class SensorServiceImp implements SensorService, ApplicationListener<SensorInactiveEvent>
{
	private final ApplicationEventPublisher eventPublisher;
	
	private final SensorRepository repository;
	
	@Autowired
	public SensorServiceImp(SensorRepository repository, ApplicationEventPublisher eventPublisher)
	{
		this.repository = repository;
		this.eventPublisher = eventPublisher;
	}
	
	@Override
	public Sensor saveOrUpdate(Sensor sensor, SensorReadings.Reading reading)
	{
		Date date = new Date();
		sensor.setLastNotificationDate(date);
		
		log.debug("------> SAVING NEW SENSOR WITH ID: " + sensor.getId());
		
		repository.save(sensor);
		
		SensorUpdateEvent event = new SensorUpdateEvent(sensor, reading);
		
		log.debug("------> PUBLISHING UPDATE EVENT WITH:" +
				"\n SENSOR ID: " + sensor.getId() +
				"\n READING: " + reading);
		
		eventPublisher.publishEvent(event);
		
		return sensor;
	}
	
	@Override
	public void onApplicationEvent(SensorInactiveEvent sensorInactiveEvent)
	{
		String sensorId = (String) sensorInactiveEvent.getSource();
		
		log.debug("RECEIVING INACTIVE EVENT WITH ID: " + sensorId);
		
		Optional sensorOptional = repository.findById(sensorId);
		
		if (sensorOptional.isPresent())
		{
			Sensor sensor = (Sensor) sensorOptional.get();
			sensor.setOnline(false);
			
			repository.save(sensor);
		}
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

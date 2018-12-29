package com.tablefootbal.server.service;

import com.tablefootbal.server.dto.ReadingDto;
import com.tablefootbal.server.entity.Sensor;
import com.tablefootbal.server.events.SensorOfflineEvent;
import com.tablefootbal.server.events.SensorUpdateEvent;
import com.tablefootbal.server.repository.SensorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Slf4j
public class SensorServiceImp implements SensorService, ApplicationListener<SensorOfflineEvent>
{
	private final ApplicationEventPublisher eventPublisher;
	
	private final SensorRepository repository;
	
	@Autowired
	public SensorServiceImp(SensorRepository repository, ApplicationEventPublisher eventPublisher)
	{
		this.repository = repository;
		this.eventPublisher = eventPublisher;
	}


//    @Override
//    public Sensor saveOrUpdate(Sensor sensor, SensorReadings.Reading reading) {
//        Date date = new Date();
//        sensor.setLastNotificationDate(date);
//
//        log.debug("------> SAVING NEW SENSOR WITH ID: " + sensor.getId());
//
//        repository.save(sensor);
//
//        SensorUpdateEvent event = new SensorUpdateEvent(sensor, reading);
//
//        log.debug("------> PUBLISHING UPDATE EVENT WITH:" +
//                "\n SENSOR ID: " + sensor.getId() +
//                "\n READING: " + reading);
//
//        eventPublisher.publishEvent(event);
//
//        return sensor;
//    }
	
	
	@Override
	public void save(Sensor sensor)
	{
		repository.save(sensor);
	}
	
	@Override
	public void saveOrUpdate(Sensor sensor, ReadingDto readingDto)
	{
		Date date = new Date();
		sensor.setLastNotificationDate(date);
		
		log.debug("------> SAVING NEW SENSOR WITH ID: " + sensor.getId());
		
		repository.save(sensor);
		
		SensorUpdateEvent event = new SensorUpdateEvent(sensor, readingDto);
		
		
		log.debug("------> PUBLISHING UPDATE EVENT WITH:" +
				"\n SENSOR ID: " + sensor.getId());
		
		eventPublisher.publishEvent(event);
	}
	
	@Override
	public void updateSensorInformation(Sensor sensor)
	{
		repository.save(sensor);
	}
	
	@Override
	public void setOccupied(String sensorId, boolean isOccupied)
	{
		Optional<Sensor> optionalSensor = repository.findById(sensorId);
		if (optionalSensor.isPresent())
		{
			Sensor sensor = optionalSensor.get();
			sensor.setOccupied(isOccupied);

			if (isOccupied)
			{
				sensor.setOnline(true);
				log.info("Sensor: " + sensorId + " is now occupied");
			}
			else
			{
				log.info("Sensor: " + sensorId + " is now inactive");
			}
			
			repository.save(sensor);
		}
	}
	
	@Override
	public void onApplicationEvent(SensorOfflineEvent sensorOfflineEvent)
	{
		String sensorId = (String) sensorOfflineEvent.getSource();
		
		log.debug("RECEIVING OFFLINE EVENT WITH ID: " + sensorId);
		
		Optional sensorOptional = repository.findById(sensorId);
		
		if (sensorOptional.isPresent())
		{
			Sensor sensor = (Sensor) sensorOptional.get();
			sensor.setOnline(false);
			
			repository.save(sensor);
		}
	}
	
	@Override
	public List<Sensor> findOccupiedSensors()
	{
		Iterable<Sensor> sensors = repository.findAll();

		return StreamSupport
				.stream(sensors.spliterator(), false)
				.filter(Sensor::isOccupied)
				.collect(Collectors.toList());
	}
	
	
	@Override
	public List<Sensor> findFreeSensors()
	{
		Iterable<Sensor> sensors = repository.findAll();
		
		return StreamSupport
				.stream(sensors.spliterator(), false)
				.filter(s -> !s.isOccupied())
				.collect(Collectors.toList());
	}
	
	@Override
	public List<Sensor> findAllOnFloor(int floor)
	{
		Iterable<Sensor> sensors = repository.findAll();
		
		return StreamSupport
				.stream(sensors.spliterator(), false)
				.filter(s -> s.getFloor() == floor)
				.collect(Collectors.toList());
	}
	
	@Override
	public List<Sensor> findAllInRoom(int room)
	{
		Iterable<Sensor> sensors = repository.findAll();
		
		return StreamSupport
				.stream(sensors.spliterator(), false)
				.filter(s -> s.getRoom() == room)
				.collect(Collectors.toList());
	}

	@Override
	public Optional<Sensor> findById(String id){
		return repository.findById(id);
	}
	
}

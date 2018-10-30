package com.tablefootbal.server.controller;

import com.tablefootbal.server.dto.SensorDto;
import com.tablefootbal.server.dto.SensorDtoValidator;
import com.tablefootbal.server.entity.Sensor;
import com.tablefootbal.server.readings.SensorReadings;
import com.tablefootbal.server.service.SensorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.xml.bind.DataBindingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/sensor")
@Slf4j
public class SensorController
{
	private DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	
	final private
	SensorService sensorService;
	
	@Autowired
	public SensorController(SensorService sensorService)
	{
		this.sensorService = sensorService;
	}
	
	@InitBinder
	protected void initBinder(WebDataBinder binder)
	{
		binder.addValidators(new SensorDtoValidator());
	}
	
	@PostMapping("/")
	public void acquireSensorData(@RequestBody @Valid SensorDto sensorDto, BindingResult result)
	{
		if(result.hasErrors())
		{
			for (ObjectError error : result.getAllErrors())
			{
				log.error(error.toString());
			}
			throw new DataBindingException(new Throwable("error.sensor_databind_error"));
		}
		
		Date date = new Date();
		int [] sensorData = sensorDto.getReadings();
		
		log.info("SENSOR DATA RECEIVED @ " + dateFormat.format(date) + " FROM " + sensorDto.getId());
		log.info("READINGS --->  X: " + sensorData[0] + " Y: " + sensorData[1] + " Z: " + sensorData[2] );
		
		Sensor sensor = new Sensor();
		sensor.setId(sensorDto.getId());
		
		long timestamp = System.currentTimeMillis();
		SensorReadings.Reading reading =
				new SensorReadings.Reading(sensorData[0], sensorData[1], sensorData[2],timestamp);
		
		sensorService.saveOrUpdate(sensor,reading);
	}
	
	@GetMapping("/")
	public String sayHello()
	{
		return "Connection Accepted";
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}

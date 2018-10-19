package com.tablefootbal.server.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/sensor")
@Slf4j
public class SensorController
{
	private DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	
	@PostMapping("/")
	public void acquireSensorData(@RequestBody String sensorData)
	{
		Date date = new Date();
		log.info("SENSOR DATA RECEIVED @ " + dateFormat.format(date) + " : " + sensorData);
	}
}

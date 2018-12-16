package com.tablefootbal.server.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tablefootbal.server.dto.ReadingDto;
import com.tablefootbal.server.dto.SensorDto;
import com.tablefootbal.server.dto.SensorDtoValidator;
import com.tablefootbal.server.entity.Sensor;
import com.tablefootbal.server.exceptions.customExceptions.InvalidPortException;
import com.tablefootbal.server.exceptions.customExceptions.InvalidSensorDataException;
import com.tablefootbal.server.service.SensorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@RestController
@RequestMapping("/sensor")
@Slf4j
public class SensorController
{
	private DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	
	final private
	SensorService sensorService;
	
	final private
	MessageSource messageSource;
	
	@Value("${server.sensor.connection.port}")
	private int sensorPort;
	
	@Autowired
	public SensorController(SensorService sensorService, MessageSource messageSource)
	{
		this.sensorService = sensorService;
		this.messageSource = messageSource;
	}
	
	@InitBinder
	protected void initBinder(WebDataBinder binder)
	{
		binder.addValidators(new SensorDtoValidator());
	}

//    @PostMapping("/")
//    public void acquireSensorData(@RequestBody @Valid SensorDto sensorDto, BindingResult result) {
//        if (result.hasErrors()) {
//            StringBuilder stringBuilder = new StringBuilder("Binding Exceptions: ");
//            for (ObjectError error : result.getAllErrors()) {
//                stringBuilder.append("\n");
//                stringBuilder.append(
//                        messageSource.getMessage(error.getCode(), null, Locale.getDefault()));
//            }
//            throw new InvalidSensorDataException(stringBuilder.toString());
//        }
//
//        Date date = new Date();
//        double[] sensorData = sensorDto.getReadings();
//
//        log.info("SENSOR DATA RECEIVED @ " + dateFormat.format(date) + " FROM " + sensorDto.getId());
//        log.info("READINGS --->  X: " + sensorData[0] + " Y: " + sensorData[1] + " Z: " + sensorData[2]);
//
//        Sensor sensor = new Sensor();
//        sensor.setId(sensorDto.getId());
//        sensor.setRoom(0);
//        sensor.setFloor(0);
//
//        long timestamp = System.currentTimeMillis();
//        SensorReadings.Reading reading =
//                new SensorReadings.Reading(sensorData[0], sensorData[1], sensorData[2], timestamp);
//
//        sensorService.saveOrUpdate(sensor, reading);
//    }
	
	@PostMapping("/")
	public void acquireSensorData(@RequestBody @Valid SensorDto sensorDto,
	                              BindingResult result,
	                              HttpServletRequest request)
	{
		if (request.getLocalPort() != sensorPort)
		{
			throw new InvalidPortException(
					messageSource.getMessage("exception.invalid_port", new Object[]{sensorPort}, Locale.getDefault()));
		}
		
		if (result.hasErrors())
		{
			StringBuilder stringBuilder = new StringBuilder("Binding Exceptions: ");
			for (ObjectError error : result.getAllErrors())
			{
				stringBuilder.append("\n");
				stringBuilder.append(
						messageSource.getMessage(error.getCode(), null, Locale.getDefault()));
			}
			throw new InvalidSensorDataException(stringBuilder.toString());
		}
		
		Date date = new Date();
		
		log.info("SENSOR DATA RECEIVED @ " + dateFormat.format(date) + " FROM " + sensorDto.getId());
		log.info("READINGS --->  X: " + sensorDto.x[0] + " Y: " + sensorDto.y[0] + " Z: " + sensorDto.z[0]);
		
		Sensor sensor = new Sensor();
		sensor.setId(sensorDto.getId());
		sensor.setRoom(0);
		sensor.setFloor(0);

//        SensorReadings.Reading reading =
//                new SensorReadings.Reading(sensorData[0], sensorData[1], sensorData[2], timestamp);
		ReadingDto readingDto = new ReadingDto(sensorDto.x, sensorDto.y, sensorDto.z, System.currentTimeMillis());
		sensorService.saveOrUpdate(sensor, readingDto);
//        sensorService.saveOrUpdate(sensor, reading);
	}
	
	@GetMapping("/")
	public String sayHello(HttpServletRequest request)
	{
		if (request.getLocalPort() != sensorPort)
		{
			throw new InvalidPortException(
					messageSource.getMessage("exception.invalid_port", new Object[]{sensorPort}, Locale.getDefault()));
		}
		
		return "Connection Accepted";
	}
	
	@ExceptionHandler(InvalidSensorDataException.class)
	public ResponseEntity<String> handleInvalidSensorData(InvalidSensorDataException exception)
	{
		log.error(exception.getMessage());
		return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(JsonProcessingException.class)
	public ResponseEntity<String> handleJsonError(JsonProcessingException exception)
	{
		log.error(exception.getMessage());
		return new ResponseEntity<>(
				messageSource.getMessage("error.invalid_json", null, Locale.getDefault()),
				HttpStatus.BAD_REQUEST);
	}
}

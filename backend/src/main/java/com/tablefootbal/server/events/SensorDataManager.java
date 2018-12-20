package com.tablefootbal.server.events;

import com.tablefootbal.server.dto.ReadingDto;
import com.tablefootbal.server.entity.CalibrationStructure;
import com.tablefootbal.server.entity.Sensor;
import com.tablefootbal.server.readings.SensorReadings;
import com.tablefootbal.server.service.SensorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.tablefootbal.server.dsp.Algorithms.applyMedianFilter;
import static com.tablefootbal.server.dsp.Algorithms.isMovement;

@Component
@PropertySource("classpath:readings.properties")
@Slf4j
public class SensorDataManager implements ApplicationListener<SensorUpdateEvent>
{
	
	@Value("${readings.axis}")
	private CalibrationStructure.Axis axis;
	
	@Value("${readings.window_size}")
	private int WINDOW_SIZE;
	
	@Value("${readings.threshold}")
	private double THRESHOLD;
	
	@Value("${readings.minAboveThresholdCount}")
	private int MIN_ABOVE_THRESHOLD_COUNT;
	
	@Value("${readings.max_readings}")
	private int MAX_READINGS;
	
	private final SensorTrackingScheduler scheduler;
	
	private Map<String, SensorReadings> readingsMap;
	
	private final SensorService sensorService;
	
	@Autowired
	public SensorDataManager(SensorTrackingScheduler scheduler, SensorService sensorService)
	{
		this.readingsMap = new HashMap<>();
		this.scheduler = scheduler;
		this.sensorService = sensorService;
	}
	
	@Override
	public void onApplicationEvent(SensorUpdateEvent sensorUpdateEvent)
	{
		
		Sensor sensor = (Sensor) sensorUpdateEvent.getSource();
		ReadingDto reading = sensorUpdateEvent.getReading();
		
		log.debug("-------> RECEIVED UPDATE EVENT WITH PARAMETERS:\n" +
				"ID: " + sensor.getId());
		
		log.debug("-------> STARTING TRACKING FOR SENSOR WITH ID: " + sensor.getId());
		
		scheduler.startTracking(sensor.getId());
		
		SensorReadings storedReadings = readingsMap.get(sensor.getId());
		
		if (storedReadings == null)
		{
			storedReadings = new SensorReadings(MAX_READINGS);
		}
		
		storedReadings.addReadingsArray(reading.getX(), reading.getY(), reading.getZ(), reading.getTimestamp());
		
		readingsMap.put(sensor.getId(), storedReadings);
		
		try
		{
			if (sensor.getCalibrationStructure().isCalibrationFlag())
			{
				performCalibration(sensor, axis);
				sensor.setActive(false);
				sensor.getCalibrationStructure().setCalibrationFlag(false);
			}
			else
			{
				boolean isActive = isMovement(storedReadings.getReadings(),
						sensor.getCalibrationStructure(),
						MIN_ABOVE_THRESHOLD_COUNT);
				sensorService.setActive(sensor.getId(), isActive);
			}
		} catch (IndexOutOfBoundsException e)
		{
			log.error("Not enough data to perform calibration or movement detection, will try with next buffer");
		} catch (Exception e)
		{
			log.error("Something went wrong during calibration or movement detection");
		}
		sensorService.save(sensor);
	}
	
	private void performCalibration(Sensor sensor, CalibrationStructure.Axis axis) throws IndexOutOfBoundsException
	{
		SensorReadings sensorReadings = readingsMap.get(sensor.getId());
		List<SensorReadings.Reading> readings = sensorReadings.getReadings();
		
		List<Double> axisReadings = getAxisReadings(readings, axis);
		
		applyMedianFilter(axisReadings, WINDOW_SIZE);
		
		double maxValue = axisReadings.stream().mapToDouble(r -> r).max().orElse(0.0);
		double minValue = axisReadings.stream().mapToDouble(r -> r).min().orElse(0.0);
		
		sensor.getCalibrationStructure().setAxis(axis);
		sensor.getCalibrationStructure().setMaxValue(maxValue);
		sensor.getCalibrationStructure().setMinValue(minValue);
		sensor.getCalibrationStructure().setThreshold(THRESHOLD);
		sensor.getCalibrationStructure().setCalibrationFlag(false);
	}
	
	public Map<String, SensorReadings> getReadingsMap()
	{
		return readingsMap;
	}
	
	public static List<Double> getAxisReadings(List<SensorReadings.Reading> readings,
	                                           CalibrationStructure.Axis axis)
	{
		List<Double> axisReadings;
		if (axis == CalibrationStructure.Axis.X)
		{
			axisReadings = readings.stream().map(SensorReadings.Reading::getX).collect(Collectors.toList());
		}
		else if (axis == CalibrationStructure.Axis.Y)
		{
			axisReadings = readings.stream().map(SensorReadings.Reading::getY).collect(Collectors.toList());
		}
		else
		{
			axisReadings = readings.stream().map(SensorReadings.Reading::getX).collect(Collectors.toList());
		}
		return axisReadings;
	}
	
}






















package com.tablefootbal.server.events;

import com.tablefootbal.server.dsp.AlgorithmParameters;
import com.tablefootbal.server.dto.ReadingDto;
import com.tablefootbal.server.entity.CalibrationStructure;
import com.tablefootbal.server.entity.Sensor;
import com.tablefootbal.server.exceptions.customExceptions.NotEnoughDataException;
import com.tablefootbal.server.readings.SensorReadings;
import com.tablefootbal.server.service.AlgorithmConfigurationService;
import com.tablefootbal.server.service.SensorService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

import static com.tablefootbal.server.dsp.Algorithms.applyMedianFilter;
import static com.tablefootbal.server.dsp.Algorithms.isMovement;

@Component
@Slf4j
@Getter
@Setter
public class SensorDataManager implements ApplicationListener<SensorUpdateEvent>
{
	private AlgorithmParameters algorithmParameters;
	
	private final SensorTrackingScheduler scheduler;
	
	private Map<String, SensorReadings> readingsMap;
	
	private final SensorService sensorService;
	
	private final AlgorithmConfigurationService algorithmService;
	
	@Autowired
	public SensorDataManager(SensorTrackingScheduler scheduler, SensorService sensorService, AlgorithmParameters algorithmParameters,
	                         AlgorithmConfigurationService algorithmService)
	{
		this.readingsMap = new HashMap<>();
		this.scheduler = scheduler;
		this.sensorService = sensorService;
		this.algorithmParameters = algorithmParameters;
		this.algorithmService = algorithmService;
	}
	
	@PostConstruct
	public void initializeAlgorithmParameters()
	{
		algorithmService.updateAlgorithmParameters(algorithmParameters);
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
			storedReadings = new SensorReadings(algorithmParameters.getMAX_READINGS());
		}
		
		storedReadings.addReadingsArray(reading.getX(), reading.getY(), reading.getZ(), reading.getTimestamp());
		
		readingsMap.put(sensor.getId(), storedReadings);
		
		try
		{
			if (sensor.getCalibrationStructure().isCalibrationFlag())
			{
				performCalibration(sensor, algorithmParameters.getAxis());
				sensor.setOccupied(false);
				sensor.getCalibrationStructure().setCalibrationFlag(false);
				log.info("Calibration finished for sensors: " + sensor.getId() + "\n");
				log.info(sensor.getCalibrationStructure().toString());
			}
			else
			{
				boolean isOccupied = isMovement(storedReadings.getReadings(),
						sensor.getCalibrationStructure(),
						algorithmParameters.getMIN_ABOVE_THRESHOLD_COUNT());
				sensor.setOccupied(isOccupied);
				log.info("Sensor: " + sensor.getId() + " occupied value has been set to " + isOccupied + "\n");
			}
		} catch (NotEnoughDataException e)
		{
			log.error("Not enough data to perform calibration or movement detection, will try with next buffer");
		} catch (Exception e)
		{
			log.error("Something went wrong during calibration or movement detection");
		}
		sensorService.save(sensor);
	}
	
	private void performCalibration(Sensor sensor, CalibrationStructure.Axis axis) throws NotEnoughDataException
	{
		SensorReadings sensorReadings = readingsMap.get(sensor.getId());
		List<SensorReadings.Reading> readings = sensorReadings.getReadings();
		
		List<Double> axisReadings = getAxisReadings(readings, axis);
		
		applyMedianFilter(axisReadings, algorithmParameters.getWINDOW_SIZE());
		
		double maxValue = axisReadings.stream().mapToDouble(r -> r).max().orElse(0.0);
		double minValue = axisReadings.stream().mapToDouble(r -> r).min().orElse(0.0);
		
		sensor.getCalibrationStructure().setAxis(axis);
		sensor.getCalibrationStructure().setMaxValue(maxValue);
		sensor.getCalibrationStructure().setMinValue(minValue);
		sensor.getCalibrationStructure().setThreshold(algorithmParameters.getTHRESHOLD());
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
			axisReadings = readings.stream().map(SensorReadings.Reading::getZ).collect(Collectors.toList());
		}
		return axisReadings;
	}
}






















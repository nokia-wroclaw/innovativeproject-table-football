package com.tablefootbal.server.dsp;

import com.tablefootbal.server.entity.CalibrationStructure;
import com.tablefootbal.server.exceptions.customExceptions.NotEnoughDataException;
import com.tablefootbal.server.readings.SensorReadings;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static com.tablefootbal.server.dsp.Algorithms.applyMedianFilter;
import static com.tablefootbal.server.dsp.Algorithms.isMovement;

@RunWith(SpringRunner.class)
public class DspAlgorithmsTests
{
	@Test(expected = NotEnoughDataException.class)
	public void givenNotEnoughData_thenMediaFilterThrows()
	{
		int windowSize = 5;
		List<Double> sourceData = Arrays.asList(1.22, 2.33);
		
		applyMedianFilter(sourceData, windowSize);
	}
	
	@Test(expected = NotEnoughDataException.class)
	public void givenNotEnoughData_thenGameDetectionThrows()
	{
		List<SensorReadings.Reading> readings = Arrays.asList(
				new SensorReadings.Reading(1.46, 0, 0, 0),
				new SensorReadings.Reading(1.99, 0, 0, 0),
				new SensorReadings.Reading(1.45, 0, 0, 0),
				new SensorReadings.Reading(1.45, 0, 0, 0));
		
		CalibrationStructure calibrationStructure = new CalibrationStructure();
		
		isMovement(readings,calibrationStructure,10);
	}
	
	@Test
	public void testMediaFilterCorrect()
	{
		int windowSize = 5;
		List<Double> sourceData = Arrays.asList(1.22, 1.43, 1.33, 1.55, 1.11, 9.00, 1.05, 1.01, 1.45, 1.44);
		List<Double> result = Arrays.asList(1.22, 1.33, 1.33, 1.43, 1.33, 1.11, 1.11, 1.44, 1.05, 1.01 );
		
		applyMedianFilter(sourceData, windowSize);
		Assert.assertThat(sourceData, is(result));
	}
	
	@Test
	public void givenNotEnoughDataAboveThreshold_thenGameDetectionReturnsFalse()
	{
		CalibrationStructure calibrationStructure = new CalibrationStructure();
		calibrationStructure.setThreshold(0.05);
		calibrationStructure.setMaxValue(1.5);
		calibrationStructure.setMinValue(1.4);
		calibrationStructure.setAxis(CalibrationStructure.Axis.X);
		
		List<SensorReadings.Reading> readings = Arrays.asList(
				new SensorReadings.Reading(1.3, 0, 0, 0),
				new SensorReadings.Reading(1.1, 0, 0, 0),
				new SensorReadings.Reading(1.45, 0, 0, 0),
				new SensorReadings.Reading(1.41, 0, 0, 0),
				new SensorReadings.Reading(1.46, 0, 0, 0),
				new SensorReadings.Reading(1.99, 0, 0, 0),
				new SensorReadings.Reading(1.45, 0, 0, 0),
				new SensorReadings.Reading(1.45, 0, 0, 0));
		
		Assert.assertFalse(isMovement(readings, calibrationStructure, 5));
	}
	
	@Test
	public void givenEnoughDataAboveThreshold_thenGameDetectionReturnsTrue()
	{
		CalibrationStructure calibrationStructure = new CalibrationStructure();
		calibrationStructure.setThreshold(0.05);
		calibrationStructure.setMaxValue(1.5);
		calibrationStructure.setMinValue(1.4);
		calibrationStructure.setAxis(CalibrationStructure.Axis.X);
		
		List<SensorReadings.Reading> readings = Arrays.asList(
				new SensorReadings.Reading(1.8, 0, 0, 0),
				new SensorReadings.Reading(1.7, 0, 0, 0),
				new SensorReadings.Reading(1.45, 0, 0, 0),
				new SensorReadings.Reading(1.41, 0, 0, 0),
				new SensorReadings.Reading(1.46, 0, 0, 0),
				new SensorReadings.Reading(1.99, 0, 0, 0),
				new SensorReadings.Reading(1.45, 0, 0, 0),
				new SensorReadings.Reading(2.45, 0, 0, 0),
				new SensorReadings.Reading(3.45, 0, 0, 0),
				new SensorReadings.Reading(4.45, 0, 0, 0));
		
		Assert.assertTrue(isMovement(readings, calibrationStructure, 5));
	}
	
	@Test
	public void givenEnoughDataBelowThreshold_thenGameDetectionReturnsTrue()
	{
		CalibrationStructure calibrationStructure = new CalibrationStructure();
		calibrationStructure.setThreshold(0.05);
		calibrationStructure.setMaxValue(2.1);
		calibrationStructure.setMinValue(2.0);
		calibrationStructure.setAxis(CalibrationStructure.Axis.X);
		
		List<SensorReadings.Reading> readings = Arrays.asList(
				new SensorReadings.Reading(1.8, 0, 0, 0),
				new SensorReadings.Reading(1.7, 0, 0, 0),
				new SensorReadings.Reading(1.45, 0, 0, 0),
				new SensorReadings.Reading(1.41, 0, 0, 0),
				new SensorReadings.Reading(1.46, 0, 0, 0),
				new SensorReadings.Reading(1.99, 0, 0, 0),
				new SensorReadings.Reading(1.45, 0, 0, 0),
				new SensorReadings.Reading(2.45, 0, 0, 0),
				new SensorReadings.Reading(3.45, 0, 0, 0),
				new SensorReadings.Reading(4.45, 0, 0, 0));
		
		Assert.assertTrue(isMovement(readings, calibrationStructure, 5));
	}
	
	@Test
	public void givenEnoughDataBelowOrAboveThreshold_thenGameDetectionReturnsTrue()
	{
		CalibrationStructure calibrationStructure = new CalibrationStructure();
		calibrationStructure.setThreshold(0.05);
		calibrationStructure.setMaxValue(2.1);
		calibrationStructure.setMinValue(2.0);
		calibrationStructure.setAxis(CalibrationStructure.Axis.X);
		
		List<SensorReadings.Reading> readings = Arrays.asList(
				new SensorReadings.Reading(2.8, 0, 0, 0),
				new SensorReadings.Reading(2.7, 0, 0, 0),
				new SensorReadings.Reading(1.45, 0, 0, 0),
				new SensorReadings.Reading(1.41, 0, 0, 0),
				new SensorReadings.Reading(1.46, 0, 0, 0),
				new SensorReadings.Reading(1.99, 0, 0, 0),
				new SensorReadings.Reading(2.0, 0, 0, 0),
				new SensorReadings.Reading(2.05, 0, 0, 0),
				new SensorReadings.Reading(2.1, 0, 0, 0),
				new SensorReadings.Reading(2.13, 0, 0, 0));
		
		Assert.assertTrue(isMovement(readings, calibrationStructure, 5));
	}
	
}





























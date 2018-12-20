package com.tablefootbal.server.dsp;

import com.tablefootbal.server.entity.CalibrationStructure;
import com.tablefootbal.server.readings.SensorReadings;

import java.util.Arrays;
import java.util.List;

import static com.tablefootbal.server.events.SensorDataManager.getAxisReadings;

public class Algorithms
{
	
	static public void applyMedianFilter(List<Double> xAxis, int windowSize)
	{
		Double[] window = new Double[windowSize];
		int windowRange = (windowSize - 1) / 2;
		
		for (int i = windowRange; i < xAxis.size() - windowRange; i++)
		{
			for (int window_index = 0; window_index < window.length; window_index++)
			{
				window[window_index] = xAxis.get(i + window_index);
			}
			Arrays.sort(window);
			xAxis.set(i, window[windowRange + 1]);
		}
	}
	
	public static boolean isMovement(List<SensorReadings.Reading> readings,
	                                 CalibrationStructure calibrationStruct,
	                                 int minSignals)
	{
		int counter = 0;
		double maxReading = calibrationStruct.getMaxValue();
		double minReading = calibrationStruct.getMaxValue();
		double threshold = calibrationStruct.getThreshold();
		
		List<Double> axisReadings = getAxisReadings(readings, calibrationStruct.getAxis());
		
		for (Double reading : axisReadings)
		{
			if (reading >=  maxReading + threshold || reading <= minReading - threshold)
			{
				counter++;
			}
		}
		return counter >= minSignals;
	}
}

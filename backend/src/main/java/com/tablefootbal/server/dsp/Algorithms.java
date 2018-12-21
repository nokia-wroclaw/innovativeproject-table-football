package com.tablefootbal.server.dsp;

import com.tablefootbal.server.entity.CalibrationStructure;
import com.tablefootbal.server.exceptions.customExceptions.NotEnoughDataException;
import com.tablefootbal.server.readings.SensorReadings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.tablefootbal.server.events.SensorDataManager.getAxisReadings;

public class Algorithms
{
	
	static public void applyMedianFilter(List<Double> axisData, int windowSize) throws NotEnoughDataException
	{
		if (windowSize > axisData.size())
		{
			throw new NotEnoughDataException();
		}
		
		List<Double> result = new ArrayList<>(axisData);
		Double[] window = new Double[windowSize];
		int windowRange = (windowSize - 1) / 2;
		int dataIndex;
		
		for (int i = 0; i < axisData.size(); i++)
		{
			int window_index = 0;
			for (int data_offset = -windowRange; data_offset < windowRange + 1; data_offset++, window_index++)
			{
				dataIndex = i + data_offset;
				if (dataIndex >= 0 && dataIndex < axisData.size())
				{
					window[window_index] = result.get(i + data_offset);
				}
				else
				{
					window[window_index] = 0.0d;
				}
			}
			Arrays.sort(window);
			axisData.set(i, window[windowRange]);
		}
	}
	
	public static boolean isMovement(List<SensorReadings.Reading> readings,
	                                 CalibrationStructure calibrationStruct,
	                                 int minSignals)
	{
		if (readings.size() < minSignals)
		{
			throw new NotEnoughDataException();
		}
		
		int counter = 0;
		double maxReading = calibrationStruct.getMaxValue();
		double minReading = calibrationStruct.getMinValue();
		double threshold = calibrationStruct.getThreshold();
		
		List<Double> axisReadings = getAxisReadings(readings, calibrationStruct.getAxis());
		
		for (Double reading : axisReadings)
		{
			if (reading >= maxReading + threshold || reading <= minReading - threshold)
			{
				counter++;
			}
		}
		return counter >= minSignals;
	}
}

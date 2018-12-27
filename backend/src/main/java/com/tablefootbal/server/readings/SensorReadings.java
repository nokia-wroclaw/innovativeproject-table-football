package com.tablefootbal.server.readings;

import lombok.Data;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;

@Data
@Slf4j
public class SensorReadings
{
	private final int MAX_READINGS;
	
	private LinkedList<Reading> readings;
	
	public SensorReadings(int max_readings)
	{
		MAX_READINGS = max_readings;
		this.readings = new LinkedList<>();
	}
	
	@ToString
	@Getter
	public static class Reading
	{
		public Reading(double x, double y, double z, long timestamp)
		{
			this.x = x;
			this.y = y;
			this.z = z;
			this.timestamp = timestamp;
		}
		
		public double x;
		public double y;
		public double z;
		long timestamp;
		
		double getReadingsSum()
		{
			return x + y + z;
		}
	}
	
	public void addReading(Reading reading)
	{
		if (readings.size() < MAX_READINGS)
		{
			readings.add(reading);
		}
		else
		{
			readings.removeFirst();
			readings.addLast(reading);
		}
	}
	
	public void addReadingsArray(double[] x, double[] y, double[] z, long timestamp)
	{
		int maxSize = Math.max(Math.max(x.length, y.length), y.length);
		
		Reading reading;
		double x_reading, y_reading, z_reading;
		for (int i = 0; i < maxSize; i++)
		{
			x_reading = (i < x.length ) ? x[i] : 0.0f;
			y_reading = (i < y.length ) ? y[i] : 0.0f;
			z_reading = (i < z.length ) ? z[i] : 0.0f;
			
			reading = new Reading(x_reading, y_reading, z_reading, timestamp);
			addReading(reading);
		}
	}
	
	public double getAverage()
	{
		double result = 0;
		for (Reading reading : readings)
		{
			result += reading.getReadingsSum();
		}
		result /= readings.size();
		
		return result;
	}
}

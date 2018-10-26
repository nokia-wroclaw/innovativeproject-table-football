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
		public Reading(int x, int y, int z, long timestamp)
		{
			this.x = x;
			this.y = y;
			this.z = z;
			this.timestamp = timestamp;
		}
		
		int x;
		int y;
		int z;
		long timestamp;
		
		int getReadingsSum()
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
	
	public int getAverage()
	{
		long result = 0;
		for (Reading reading : readings)
		{
			result += reading.getReadingsSum();
		}
		result /= readings.size();
		
		return (int) result;
	}
}

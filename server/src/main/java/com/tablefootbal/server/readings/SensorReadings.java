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
		
		double x;
		double y;
		double z;
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

	/*public boolean isMovement(){
		double threshold = 2;
		double value = this.z;
		long startTime = System.currentTimeMilis();
		long elapsedTime = null;
		int counter = null;
		long waitTime = 10;
		int minSignals = 10;

		do{
		    elapsedTime=System.currentTime() - startTime;
            if(value>=threshold){
                counter++;
            }
        }while(elapsedTime<waitTime){};

		if(counter>=minSignals && elapsedTime<=waitTime) {
            return true;
        }else{
		    return false;
        }
	}*/
}

package com.tablefootbal.server.dto;

import lombok.Getter;

@Getter
public class SensorDto
{
	public SensorDto()
	{
	}
	
	public SensorDto(String id, int[] readings)
	{
		this.id = id;
		this.readings = readings;
	}
	
	String id;
	int[] readings = new int[3];
}

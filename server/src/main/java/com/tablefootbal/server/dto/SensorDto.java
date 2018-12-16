package com.tablefootbal.server.dto;

import lombok.Getter;

@Getter
public class SensorDto
{
	String id;
	public double[] x;
	public double[] y;
	public double[] z;
}

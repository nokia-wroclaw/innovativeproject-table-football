package com.tablefootbal.server.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CalibrationStructure
{
	public enum Axis
	{
		X, Y, Z
	}
	
	public CalibrationStructure()
	{
		double maxValue = 10.0;
		double minValue = -10.0;
		threshold = 0.0;
		calibrationFlag = true;
		axis = Axis.Z;
	}
	
	Axis axis;
	double maxValue;
	double minValue;
	double threshold;
	boolean calibrationFlag;
}

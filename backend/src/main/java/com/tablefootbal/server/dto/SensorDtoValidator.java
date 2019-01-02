package com.tablefootbal.server.dto;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class SensorDtoValidator implements Validator
{
	private static final String macAddressPattern = "^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$";



	@Override
	public boolean supports(Class<?> aClass)
	{
		return SensorDto.class.equals(aClass);
	}
	
	@Override
	public void validate(Object o, Errors errors)
	{
		SensorDto sensorDto = (SensorDto) o;
		
		if (null == sensorDto.id)
		{
			errors.rejectValue("id", "error.id_null");
		}
		else if (!sensorDto.id.matches(macAddressPattern))
		{
			//mac check
			errors.rejectValue("id", "error.id_not_valid");
		}
//		if (null == sensorDto.readings || sensorDto.readings.length < 3)
//		{
//			errors.rejectValue("readings", "error.reading_missing");
//		}
	}
}

package com.tablefootbal.server.service;

import com.tablefootbal.server.dsp.AlgorithmParameters;
import com.tablefootbal.server.entity.CalibrationStructure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@PropertySource("classpath:readings.properties")
public class AlgorithmConfigurationServiceImp implements AlgorithmConfigurationService
{
	@Value("${readings.parameters_redis_key}")
	private String parametersKey;
	private final String MIN_COUNT_KEY = "MIN_COUNT";
	private final String THRESHOLD_KEY = "THRESHOLD";
	private final String WINDOW_SIZE_KEY = "WINDOW_SIZE";
	private final String MAX_READINGS_KEY = "MAX_READINGS";
	private final String AXIS_KEY = "AXIS";
	
	final private
	RedisTemplate<String, Object> redisTemplate;
	private HashOperations<String, String, Object> hashOperations;
	
	@Autowired
	public AlgorithmConfigurationServiceImp(RedisTemplate<String, Object> redisTemplate)
	{
		this.redisTemplate = redisTemplate;
		this.hashOperations = redisTemplate.opsForHash();
	}
	
	
	@Override
	public void updateAlgorithmParameters(AlgorithmParameters parameters)
	{
		Boolean parametersFound = redisTemplate.hasKey(parametersKey);
		if (!(null == parametersFound) && parametersFound)
		{
			String temp;
			
			temp = (String) hashOperations.get(parametersKey, MIN_COUNT_KEY);
			if (null != temp)
			{
				parameters.setMIN_ABOVE_THRESHOLD_COUNT(Integer.parseInt(temp));
			}
			
			temp = (String) hashOperations.get(parametersKey, MAX_READINGS_KEY);
			if (null != temp)
			{
				parameters.setMAX_READINGS(Integer.parseInt(temp));
			}
			
			temp = (String) hashOperations.get(parametersKey, WINDOW_SIZE_KEY);
			if (null != temp)
			{
				parameters.setWINDOW_SIZE(Integer.parseInt(temp));
			}
			
			temp = (String) hashOperations.get(parametersKey, THRESHOLD_KEY);
			if (null != temp)
			{
				parameters.setTHRESHOLD(Double.parseDouble(temp));
			}
			
			temp = (String) hashOperations.get(parametersKey, AXIS_KEY);
			if (null != temp)
			{
				parameters.setAxis(CalibrationStructure.Axis.valueOf(temp));
			}
		}
	}
	
	@Override
	public void saveAlgorithmParameters(AlgorithmParameters parameters)
	{
		final Map<String, Object> values = new HashMap<>();
		values.put(MAX_READINGS_KEY, parameters.getMAX_READINGS());
		values.put(MIN_COUNT_KEY, parameters.getMIN_ABOVE_THRESHOLD_COUNT());
		values.put(WINDOW_SIZE_KEY, parameters.getWINDOW_SIZE());
		values.put(THRESHOLD_KEY, parameters.getTHRESHOLD());
		values.put(AXIS_KEY, parameters.getAxis());
		
		hashOperations.putAll(parametersKey, values);
	}
	
	@Override
	public void clearAlgorithmParameters()
	{
		redisTemplate.delete(parametersKey);
	}
}

package com.tablefootbal.server;

import com.tablefootbal.server.dsp.AlgorithmParameters;
import com.tablefootbal.server.entity.CalibrationStructure;
import com.tablefootbal.server.service.AlgorithmConfigurationService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class AlgorithmPropertiesServiceIntegrationTests
{
	@Autowired
	private AlgorithmConfigurationService service;
	
	@Test
	public void testAlgorithmParametersSerializeAndDeserializeCorrectly()
	{
		AlgorithmParameters params = new AlgorithmParameters();
		params.setTHRESHOLD(0.05);
		params.setMAX_READINGS(32);
		params.setMIN_ABOVE_THRESHOLD_COUNT(10);
		params.setWINDOW_SIZE(5);
		params.setAxis(CalibrationStructure.Axis.Z);
		
		service.saveAlgorithmParameters(params);
		AlgorithmParameters retrievedParams = new AlgorithmParameters();
		
		service.updateAlgorithmParameters(retrievedParams);
		
		Assert.assertEquals(retrievedParams,params);
	}
	
	@After
	public void deleteAlgorithmParameters()
	{
		service.clearAlgorithmParameters();
	}
}

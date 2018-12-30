package com.tablefootbal.server.service;

import com.tablefootbal.server.dsp.AlgorithmParameters;

public interface AlgorithmConfigurationService
{
	void updateAlgorithmParameters(AlgorithmParameters parameters);
	
	void saveAlgorithmParameters(AlgorithmParameters parameters);
	
	void clearAlgorithmParameters();
}

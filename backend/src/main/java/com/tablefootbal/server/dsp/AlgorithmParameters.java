package com.tablefootbal.server.dsp;

import com.tablefootbal.server.entity.CalibrationStructure;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@PropertySource("classpath:readings.properties")
@Setter
@Getter
@EqualsAndHashCode
@RedisHash("algorithm_parameters")
public class AlgorithmParameters implements Serializable
{
	@Value("${readings.axis}")
	CalibrationStructure.Axis axis;
	
	@Value("${readings.window_size}")
	private int WINDOW_SIZE;
	
	@Value("${readings.threshold}")
	private double THRESHOLD;
	
	@Value("${readings.minAboveThresholdCount}")
	private int MIN_ABOVE_THRESHOLD_COUNT;
	
	@Value("${readings.max_readings}")
	private int MAX_READINGS;

	@Value("${readings.number_of_states_to_swap}")
	private int NUM_OF_STATES_TO_SWAP;

	@Value("${readings.seconds_until_offline}")
	private int SECONDS_UNTIL_OFFLINE;
}

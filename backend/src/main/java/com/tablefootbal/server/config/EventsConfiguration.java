package com.tablefootbal.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
public class EventsConfiguration
{
	@Bean
	public ThreadPoolTaskScheduler threadPoolTaskScheduler()
	{
		return new ThreadPoolTaskScheduler();
	}
	
}

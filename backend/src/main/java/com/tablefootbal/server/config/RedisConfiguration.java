package com.tablefootbal.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfiguration
{
	@Bean
	LettuceConnectionFactory lettuceConnectionFactory()
	{
		return new LettuceConnectionFactory();
	}

	@Bean
	RedisTemplate<String, Object> redisTemplate()
	{
		final RedisTemplate<String, Object> template = new RedisTemplate<>();
		template.setConnectionFactory(lettuceConnectionFactory());
		template.setKeySerializer(new StringRedisSerializer());
		template.setHashValueSerializer(new GenericToStringSerializer<>(Object.class));
		template.setValueSerializer(new GenericToStringSerializer<>(Object.class));
		return template;
	}
}

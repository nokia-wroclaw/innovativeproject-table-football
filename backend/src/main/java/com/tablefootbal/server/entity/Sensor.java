package com.tablefootbal.server.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.Date;

@Setter
@Getter
@RedisHash("sensors")
public class Sensor implements Serializable
{
	String id;
	
	boolean active;

	boolean online;
	
	Date lastNotificationDate;
	
	int floor;
	
	int room;
}

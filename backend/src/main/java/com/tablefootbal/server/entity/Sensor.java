package com.tablefootbal.server.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
	public Sensor(String id, boolean active, boolean online, Date lastNotificationDate)
	{
		this.id = id;
		this.active = active;
		this.online = online;
		this.lastNotificationDate = lastNotificationDate;
		this.calibrationStructure = new CalibrationStructure();
	}
	
	public Sensor()
	{
		this.calibrationStructure = new CalibrationStructure();
	}
	
	String id;
	
	boolean active;
	
	boolean online;
	
	Date lastNotificationDate;
	
	int floor;
	
	int room;
	
	@JsonIgnore
	CalibrationStructure calibrationStructure;
}

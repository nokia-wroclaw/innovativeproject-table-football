package com.tablefootbal.server.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Setter
@Getter
@Table(name = "sensors")
public class Sensor
{
	@Id
	String id;
	
	@Column(length = 1)
	boolean active;
	@Column(length = 1)
	boolean online;
	
	@Column
	@Temporal(TemporalType.TIMESTAMP)
	Date lastNotificationDate;
	
	@Column
	int floor;
	
	@Column
	int room;
}

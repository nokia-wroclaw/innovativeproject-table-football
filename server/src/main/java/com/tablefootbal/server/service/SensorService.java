package com.tablefootbal.server.service;

import com.tablefootbal.server.entity.Sensor;

import java.util.List;

public interface SensorService
{
	boolean checkIfExistsById(String id);
	
	Sensor saveOrUpdate(Sensor sensor);
	
	List<Sensor> findActiveSensors();
	
	List<Sensor> findUnactiveSensors();
	
	List<Sensor> findConnectedSensors();
	
	List<Sensor> findConnected();
	
	List<Sensor> findDisconnected();
	
	List<Sensor> findAllOnFloor(int floor);
	
	List<Sensor> findAllInRoom(int room);
}

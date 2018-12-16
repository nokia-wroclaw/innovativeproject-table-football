package com.tablefootbal.server.repository;

import com.tablefootbal.server.entity.Sensor;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SensorRepository extends CrudRepository<Sensor, String>
{
	List<Sensor> findAllByActiveIsTrue();
	
	List<Sensor> findAllByActiveIsFalse();
	
	List<Sensor> findAllByOnlineIsTrue();
	
	List<Sensor> findAllByOnlineIsFalse();
	
	List<Sensor> findAllByFloor(int floor);
	
	List<Sensor> findAllByRoom(int room);
	
}

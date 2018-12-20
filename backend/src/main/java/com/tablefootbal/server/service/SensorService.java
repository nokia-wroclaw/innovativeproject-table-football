package com.tablefootbal.server.service;

import com.tablefootbal.server.dto.ReadingDto;
import com.tablefootbal.server.entity.Sensor;

public interface SensorService {
//    boolean checkIfExistsById(String id);

//    Sensor saveOrUpdate(Sensor sensor, SensorReadings.Reading reading);
    
     void saveOrUpdate(Sensor sensor, ReadingDto readingDto);

    void updateSensorInformation(Sensor sensor);

    void setActive(String SensorId, boolean isActive);
    
    void save(Sensor sensor);

//    List<Sensor> findActiveSensors();
//
//    List<Sensor> findUnactiveSensors();
//
//    List<Sensor> findConnectedSensors();
//
//    List<Sensor> findConnected();
//
//    List<Sensor> findDisconnected();
//
//    List<Sensor> findAllOnFloor(int floor);
//
//    List<Sensor> findAllInRoom(int room);
}

package com.tablefootbal.server.service;

import com.tablefootbal.server.entity.Sensor;
import com.tablefootbal.server.readings.SensorReadings;

import java.util.List;

public interface SensorService {
//    boolean checkIfExistsById(String id);

    Sensor saveOrUpdate(Sensor sensor, SensorReadings.Reading reading);

    void updateSensorInformation(Sensor sensor);

    void setActive(String SensorId, boolean isActive);

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

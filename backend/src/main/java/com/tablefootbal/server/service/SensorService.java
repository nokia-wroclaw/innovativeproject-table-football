package com.tablefootbal.server.service;

import com.tablefootbal.server.dto.ReadingDto;
import com.tablefootbal.server.entity.Sensor;

import java.util.List;
import java.util.Optional;

public interface SensorService {


//    Sensor saveOrUpdate(Sensor sensor, SensorReadings.Reading reading);


    void saveOrUpdate(Sensor sensor, ReadingDto readingDto);


    void updateSensorInformation(Sensor sensor);

    void setOccupied(String SensorId, boolean isOccupied);

    void save(Sensor sensor);

    List<Sensor> findOccupiedSensors();

    List<Sensor> findFreeSensors();

    List<Sensor> findAllOnFloor(int floor);

    List<Sensor> findAllInRoom(int room);

    Optional<Sensor> findById(String id);


    //    boolean checkIfExistsById(String id);
//    Sensor saveOrUpdate(Sensor sensor, SensorReadings.Reading reading);
//
//    List<Sensor> findConnectedSensors();
//
//    List<Sensor> findConnected();
//
//    List<Sensor> findDisconnected();
//
    void markAllForCalibration();
}

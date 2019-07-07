package com.tablefootbal.server.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@RedisHash("sensors")
public class Sensor implements Serializable {
    public Sensor(String id, boolean occupied, boolean online, Date lastNotificationDate) {
        this.id = id;
        this.occupied = occupied;
        this.online = online;
        this.lastNotificationDate = lastNotificationDate;
        this.calibrationStructure = new CalibrationStructure();
    }

    public Sensor() {
        this.calibrationStructure = new CalibrationStructure();
    }

    String id;
    boolean occupied;
    boolean online;
    Date lastNotificationDate;
    float vcc;
    int floor;
    int room;

    @JsonIgnore
    CalibrationStructure calibrationStructure;
}

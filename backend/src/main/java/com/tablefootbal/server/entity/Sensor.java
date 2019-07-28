package com.tablefootbal.server.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tablefootbal.server.dto.VoltageReading;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

@RedisHash("sensors")
public class Sensor implements Serializable {
    public Sensor(String id, boolean occupied, boolean online, Date lastNotificationDate) {
        this.id = id;
        this.occupied = occupied;
        this.online = online;
        this.lastNotificationDate = lastNotificationDate;
        this.calibrationStructure = new CalibrationStructure();
        this.voltageReadings = new ArrayList<>();
    }

    public Sensor() {
        this.calibrationStructure = new CalibrationStructure();
        this.voltageReadings = new ArrayList<>();
    }

    public void addReading(VoltageReading volageReading) {
        LocalDate oldestDate = LocalDate.now().minusDays(voltageLimitDays);

        voltageReadings.removeIf(reading ->
                Instant.ofEpochMilli(reading.getDate().getTime())
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate().isBefore(oldestDate)
        );

        voltageReadings.add(volageReading);
    }

    @Value("${readings.keep_voltage_days}")
    private int voltageLimitDays;

    @Getter
    @Setter
    String id;

    @Getter
    @Setter
    boolean occupied;

    @Getter
    @Setter
    boolean online;

    @Getter
    @Setter
    Date lastNotificationDate;

    @Getter
    @Setter
    int floor;

    @Getter
    @Setter
    int room;

    @JsonIgnore
    @Getter
    @Setter
    CalibrationStructure calibrationStructure;

    @Getter
    @Setter
    ArrayList<VoltageReading> voltageReadings;
}

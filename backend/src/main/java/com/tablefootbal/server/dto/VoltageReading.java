package com.tablefootbal.server.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class VoltageReading implements Serializable {
    float vcc;
    Date date;

    public VoltageReading(float vcc, Date date) {
        this.vcc = vcc;
        this.date = date;
    }
}

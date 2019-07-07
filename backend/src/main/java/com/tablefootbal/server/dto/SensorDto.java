package com.tablefootbal.server.dto;

import lombok.Getter;

import java.util.Arrays;

@Getter
public class SensorDto {
    String id;
    int vcc;
    public double[] x;
    public double[] y;
    public double[] z;

    @Override
    public String toString() {
        return "\n" +
                "vcc= " + vcc / 1024f + "\n" +
                "x=" + Arrays.toString(x) + "\n" +
                "y=" + Arrays.toString(y) + "\n" +
                "z=" + Arrays.toString(z) + "\n";
    }
}

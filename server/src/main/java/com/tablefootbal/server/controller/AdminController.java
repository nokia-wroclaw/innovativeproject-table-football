package com.tablefootbal.server.controller;

import com.tablefootbal.server.entity.Sensor;
import com.tablefootbal.server.service.SensorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@CrossOrigin
public class AdminController {

    final
    private SensorService sensorService;

    @Autowired
    public AdminController(SensorService sensorService) {
        this.sensorService = sensorService;
    }

    @GetMapping("/")
    public String ConfirmAdmin() {
        return "Admin confirmed";
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void updateSensors(@RequestBody List<Sensor> sensors) {

        for (Sensor sensor : sensors) {
            sensorService.updateSensorInformation(sensor);
        }
    }
}

package com.tablefootbal.server.controller;

import com.tablefootbal.server.entity.Sensor;
import com.tablefootbal.server.exceptions.customExceptions.SensorNotFoundException;
import com.tablefootbal.server.service.SensorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@RestController
@RequestMapping("/admin")
public class AdminController {

    final
    private SensorService sensorService;
    
    final private MessageSource messageSource;

    @Autowired
    public AdminController(SensorService sensorService, MessageSource messageSource) {
        this.sensorService = sensorService;
        this.messageSource = messageSource;
    }

    @GetMapping("/")
    @CrossOrigin
    public String ConfirmAdmin() {
        return "Admin confirmed";
    }

    @PostMapping
    @CrossOrigin
    @ResponseStatus(HttpStatus.OK)
    public void updateSensors(@RequestBody List<Sensor> sensors) {
        for (Sensor sensor : sensors) {
            sensorService.updateSensorInformation(sensor);
        }
    }
    
    @PostMapping("/calibration/{sensorId}")
    @CrossOrigin
    public void setCalibrationFlag(@PathVariable("sensorId") String sensorId) {
    	Optional<Sensor> sensorOptional = sensorService.findById(sensorId);
    	if(sensorOptional.isPresent())
        {
            Sensor sensor = sensorOptional.get();
            sensor.getCalibrationStructure().setCalibrationFlag(true);
            sensorService.save(sensor);
        }
    	else
        {
            throw new SensorNotFoundException(messageSource.getMessage(
                    "exception.sensorNotFound",new Object [] {sensorId}, Locale.getDefault()));
        }
    }
}

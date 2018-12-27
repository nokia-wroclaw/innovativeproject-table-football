package com.tablefootbal.server.controller;

import com.tablefootbal.server.entity.Sensor;
import com.tablefootbal.server.repository.SensorRepository;
import com.tablefootbal.server.service.SensorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/")
@CrossOrigin
public class TableStatusController {

    final private
    SensorRepository repository;
    
    final private
    SensorService sensorService;

    @Autowired
    public TableStatusController(SensorRepository repository, SensorService sensorService) {
        this.repository = repository;
        this.sensorService = sensorService;
    }

    @GetMapping("/")
    public String showTableStatusPage() {
        return "status";
    }

    @GetMapping("/sensorStatus")
    @ResponseBody
    public List<Sensor> getSensorsStatus() {
        Iterable<Sensor> iterable = repository.findAll();
        List<Sensor> list = new ArrayList<>();
        iterable.forEach(list::add);
        return list;
    }
    
    @GetMapping("/sensorStatus/occupied")
    @ResponseBody
    public List<Sensor> getOccupiedSensors() {
    	return sensorService.findOccupiedSensors();
    }
    
    @GetMapping("/sensorStatus/free")
    @ResponseBody
    public List<Sensor> getFreeSensors() {
        return sensorService.findFreeSensors();
    }
    
    @GetMapping("/sensorStatus/room/{room}")
    @ResponseBody
    public List<Sensor> getAllInRoom(@PathVariable("room") int room) {
        return sensorService.findAllInRoom(room);
    }
    
    @GetMapping("/sensorStatus/floor/{floor}")
    @ResponseBody
    public List<Sensor> getAllOnFloor(@PathVariable("floor") int floor) {
        return sensorService.findAllOnFloor(floor);
    }
}























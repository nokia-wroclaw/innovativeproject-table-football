package com.tablefootbal.server.controller;

import com.tablefootbal.server.entity.Sensor;
import com.tablefootbal.server.repository.SensorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
public class TableStatusController {

    final
    SensorRepository repository;

    @Autowired
    public TableStatusController(SensorRepository repository) {
        this.repository = repository;
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
}

package com.tablefootbal.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tablefootbal.server.entity.Sensor;
import com.tablefootbal.server.repository.SensorRepository;
import com.tablefootbal.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.File;
import java.nio.file.Files;
import java.util.List;

@SpringBootApplication
@EnableWebSecurity
@EnableWebMvc
@EnableScheduling
@EnableRedisRepositories
public class ServerApplication {
    @Autowired
    SensorRepository sensorRepository;

    @Autowired
    UserRepository userRepository;

    @EventListener(ApplicationStartedEvent.class)
    public void initialize() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ClassPathResource resource = new ClassPathResource("sensors.json");
        Sensor[] sensors = mapper.readValue(resource.getURL().openStream(), Sensor[].class);

        for (Sensor s : sensors) {
            sensorRepository.save(s);
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }
}

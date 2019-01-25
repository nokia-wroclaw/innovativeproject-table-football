package com.tablefootbal.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tablefootbal.server.entity.Sensor;
import com.tablefootbal.server.entity.User;
import com.tablefootbal.server.entity.UserRole;
import com.tablefootbal.server.repository.SensorRepository;
import com.tablefootbal.server.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;

@Component
@Slf4j
public class InitializationManager implements ApplicationListener<ApplicationStartedEvent> {
    @Autowired
    SensorRepository sensorRepository;

    @Autowired
    UserRepository userRepository;

    @Value("${spring.security.user.name}")
    private String sensorName;

    @Value("${spring.security.user.password}")
    private String sensorPassword;

    @Value("${admin.user.name}")
    private String adminUsername;

    @Value("${admin.password}")
    private String adminPassword;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {

        ObjectMapper mapper = new ObjectMapper();

        try {
            ClassPathResource resource = new ClassPathResource("sensors.json");
            Sensor[] sensors = mapper.readValue(resource.getURL().openStream(), Sensor[].class);

            for (Sensor s : sensors) {
                sensorRepository.save(s);
            }
        } catch (IOException e) {
            log.error("Cannot load dummy sensors from file");
        }

        User user = new User();
        user.setUsername(sensorName);
        user.setPassword(passwordEncoder.encode(sensorPassword));

        UserRole role = new UserRole();
        role.setName(UserRole.USER_ROLES.ROLE_SENSOR);

        ArrayList<UserRole> roles = new ArrayList<>();
        roles.add(role);

        user.setRoles(roles);
        userRepository.save(user);

        user = new User();
        user.setUsername(adminUsername);
        user.setPassword(passwordEncoder.encode(adminPassword));

        role = new UserRole();
        role.setName(UserRole.USER_ROLES.ROLE_ADMIN);

        roles = new ArrayList<>();
        roles.add(role);

        user.setRoles(roles);
        userRepository.save(user);

    }
}

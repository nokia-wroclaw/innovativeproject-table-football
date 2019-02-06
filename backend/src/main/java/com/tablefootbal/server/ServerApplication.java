package com.tablefootbal.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebSecurity
@EnableWebMvc
@EnableScheduling
public class ServerApplication {

    public static void main(String[] args) {
        ApplicationContext context =
                SpringApplication.run(ServerApplication.class, args);

        for (String name : context.getBeanDefinitionNames())
            System.out.println(name);

    }
}


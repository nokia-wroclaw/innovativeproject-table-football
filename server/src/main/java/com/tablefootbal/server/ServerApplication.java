package com.tablefootbal.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebSecurity
@EnableWebMvc
public class ServerApplication
{
	public static void main(String[] args)
	{
		SpringApplication.run(ServerApplication.class, args);
	}
}

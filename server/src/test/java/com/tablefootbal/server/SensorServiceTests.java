package com.tablefootbal.server;

import com.tablefootbal.server.service.SensorService;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SensorServiceTests
{
	@Autowired
	SensorService service;
}

package com.tablefootbal.server;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.springframework.boot.test.context.SpringBootTest;

@RunWith(Suite.class)
@SpringBootTest
@Suite.SuiteClasses({SensorRepositoryIntegrationTests.class, SensorServiceTests.class,
SensorReadingsTests.class, SensorDataManagerIntegrationTests.class})

public class ServerApplicationTests {

	@Test
	public void contextLoads() {
	}

}

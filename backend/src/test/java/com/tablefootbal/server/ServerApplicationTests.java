package com.tablefootbal.server;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@RunWith(Suite.class)
@SpringBootTest
@Suite.SuiteClasses({SensorRepositoryIntegrationTests.class,
        SensorReadingsTests.class})
public class ServerApplicationTests {

    @Test
    public void contextLoads() {
    }

}

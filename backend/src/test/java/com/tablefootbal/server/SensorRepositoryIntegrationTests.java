package com.tablefootbal.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tablefootbal.server.entity.Sensor;
import com.tablefootbal.server.repository.SensorRepository;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SensorRepositoryIntegrationTests {
    @Autowired
    public SensorRepository repository;

    private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private List<Sensor> addedSensors = new ArrayList<>();

    @Before
    public void populateData() throws Exception {
//        Sensor sensor;
//        sensor = getSensor("08:3F:33:33:3D:30", true, true, "2018-10-22 18:22:30", 1, 123);
//        addedSensors.add(sensor);
//        sensor = getSensor("03:F3:E3:A3:F3:C3", false, true, "2018-10-20 17:20:20", 1, 125);
//        addedSensors.add(sensor);
//        sensor = getSensor("02:F2:D2:32:F2:C2", false, false, "2018-10-22 18:22:30", 2, 222);
//        addedSensors.add(sensor);
//        sensor = getSensor("28:2F:23:23:2D:20", true, true, "2018-10-30 20:11:44", 2, 222);
//        addedSensors.add(sensor);
//        sensor = getSensor("18:1F:13:13:1D:10", false, true, "2018-09-22 11:22:31", 3, 331);
//        addedSensors.add(sensor);
//        sensor = getSensor("01:F1:D1:31:F1:C1", false, false, "2018-10-10 10:15:10", 3, 385);
//        addedSensors.add(sensor);
//
//        for (Sensor s : addedSensors) {
//            repository.save(s);
//        }
        ObjectMapper mapper = new ObjectMapper();
        ClassPathResource resource = new ClassPathResource("sensors.json");
        Sensor[] sensors = mapper.readValue(resource.getURL().openStream(), Sensor[].class);
        for(Sensor s : sensors)
        {
            repository.save(s);
        }
    }

    @After
    public void deleteData() {
        for (Sensor s : addedSensors) {
            repository.delete(s);
        }
    }

    public Sensor getSensor(String id, boolean active, boolean online, String date, int floor, int room) throws Exception {
        Sensor sensor = new Sensor();
        sensor.setId(id);
        sensor.setOccupied(active);
        sensor.setOnline(online);
        sensor.setFloor(floor);
        sensor.setRoom(room);
        sensor.setLastNotificationDate(dateFormat.parse(date));

        return sensor;
    }

    @Test
    public void whenCountCalled_Then6Returned() {
        Assert.assertEquals(repository.count(), 6);
    }

//    @Test
//    public void whenQueriedForActive_Then2EntriesReturned() {
//        List<Sensor> result = repository.findAllByActiveIsTrue();
//        Assert.assertEquals(result.size(), 2);
//
//        for (Sensor sensor : result) {
//            Assert.assertTrue(sensor.isOccupied());
//        }
//    }
//
//    @Test
//    public void whenQueriedForOffline_Then2EntriesReturned() {
//        List<Sensor> result = repository.findAllByOnlineIsFalse();
//        Assert.assertEquals(result.size(), 2);
//
//        for (Sensor sensor : result) {
//            Assert.assertFalse(sensor.isOnline());
//        }
//    }
//
//    @Test
//    public void whenQueriedForOnline_Then4EntriesReturned() {
//        List<Sensor> result = repository.findAllByOnlineIsTrue();
//        Assert.assertEquals(result.size(), 4);
//
//        for (Sensor sensor : result) {
//            Assert.assertTrue(sensor.isOnline());
//        }
//    }
//
//    @Test
//    public void whenQueriedForFloor3_Then2EntriesReturned() {
//        List<Sensor> result = repository.findAllByFloor(3);
//
//        for (Sensor sensor : result) {
//            Assert.assertEquals(sensor.getFloor(), 3);
//        }
//    }
//
//    @Test
//    public void whenQueriedForRoom222_Then2EntriesReturned() {
//        List<Sensor> result = repository.findAllByRoom(222);
//
//        for (Sensor sensor : result) {
//            Assert.assertEquals(sensor.getRoom(), 222);
//        }
//    }

    @Test
    public void testNewSensorSaved() {
        Sensor sensor = new Sensor();
        sensor.setId("FD:DD:84:21:C2:11");

        repository.save(sensor);

        Optional<Sensor> result = repository.findById(sensor.getId());

        Assert.assertEquals(sensor.getId(), result.get().getId());

        repository.delete(sensor);

    }


}

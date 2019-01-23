package com.tablefootbal.server.notification;

import com.tablefootbal.server.entity.Sensor;
import com.tablefootbal.server.notifications.KeyStorage;
import com.tablefootbal.server.notifications.dto.Notification;
import com.tablefootbal.server.notifications.dto.Push;
import com.tablefootbal.server.notifications.service.NotificationSender;
import com.tablefootbal.server.notifications.service.NotificationService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;



@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@TestPropertySource("classpath:firebase.properties")
public class NotificationSenderTest {

    private String device_token = KeyStorage.deviceToken;

    private Sensor sensor;
    private Push push;

    @Value("${fcm_key}")
    private String fcm_key;

    @Value("${fcm_endpoint}")
    private String firebase_endpoint;

    private NotificationService service = new NotificationSender();

    @Before
    public void setUp() throws Exception {
        sensor = getSensor("08:3F:33:33:3D:30", true, true, "2018-10-22 18:22:30", 1, 123);
        Notification notification = new Notification();
        notification.setBody("world!");
        notification.setTitle("Hello");

        push = new Push();
        push.setNotification(notification);
        push.setPriority("high");
        push.setTo(device_token);

        ReflectionTestUtils.setField(service, "fcmKey", fcm_key);
        ReflectionTestUtils.setField(service, "FCM_API", firebase_endpoint);

    }

    @Test
    public  void shouldBeSuccessful(){
        Assert.assertEquals((int) service.sendNotification(push).getSuccess(), 1);
    }

//    @Test
//    public void shouldNotifyObserverOnMatchStopped() throws Exception {
//        Sensor activeSensor = getSensor("08:3F:33:33:3D:32" ,true, true, "2018-10-22 18:22:30", 1, 337);
//    }

    public Sensor getSensor(String id, boolean active, boolean online, String date, int floor, int room) throws Exception {
        Sensor sensor = new Sensor();
        sensor.setId(id);
        sensor.setOccupied(active);
        sensor.setOnline(online);
        sensor.setFloor(floor);
        sensor.setRoom(room);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sensor.setLastNotificationDate(dateFormat.parse(date));

        return sensor;
    }
}

/*
{
    "multicast_id":5569016991526268362,
    "success":1,
    "failure":0,
    "canonical_ids":0,
    "results":[{
        "message_id":"0:1544423161702872%f63a77b3f9fd7ecd"
        }]
     }

 */
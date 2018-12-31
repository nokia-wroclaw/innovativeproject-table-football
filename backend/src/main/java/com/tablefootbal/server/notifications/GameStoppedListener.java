package com.tablefootbal.server.notifications;

import com.tablefootbal.server.events.PlayStoppedEvent;
import com.tablefootbal.server.notifications.dto.Notification;
import com.tablefootbal.server.notifications.dto.Push;
import com.tablefootbal.server.notifications.service.NotificationSender;
import com.tablefootbal.server.notifications.service.NotificationService;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class GameStoppedListener implements ApplicationListener<PlayStoppedEvent> {

    @Override
    public void onApplicationEvent(PlayStoppedEvent event) {

        String sensorId = event.getSource().toString();

        NotificationService service = new NotificationSender();

        Notification notification = new Notification();
        notification.setTitle("Zmiana statusu");
        notification.setBody("Stop meczu na stole:" + sensorId);
        Push push = new Push();
        push.setTo(KeyStorage.deviceToken);
        push.setNotification(notification);
        push.setPriority("HIGH");

        service.sendNotification(push);

    }
}

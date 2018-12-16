package com.tablefootbal.server.notifications.service;

import com.tablefootbal.server.entity.Sensor;
import com.tablefootbal.server.notifications.dto.Push;
import com.tablefootbal.server.notifications.entity.FirebaseResponse;

public interface NotificationService {

    FirebaseResponse sendNotification(Push push);

}

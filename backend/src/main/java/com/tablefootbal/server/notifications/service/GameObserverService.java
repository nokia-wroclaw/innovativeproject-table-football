package com.tablefootbal.server.notifications.service;

import com.tablefootbal.server.notifications.entity.GameObserver;

public interface GameObserverService {

    void register(GameObserver observer, String[] sensor_IDs);

    void notifyObservers(String sensorId);
}

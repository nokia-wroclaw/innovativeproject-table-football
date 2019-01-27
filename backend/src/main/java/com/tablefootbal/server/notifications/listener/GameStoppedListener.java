package com.tablefootbal.server.notifications.listener;

import com.tablefootbal.server.events.PlayStoppedEvent;
import com.tablefootbal.server.notifications.service.GameObserverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class GameStoppedListener implements ApplicationListener<PlayStoppedEvent> {

    private final GameObserverService service;

    @Autowired
    public GameStoppedListener(GameObserverService service){
        this.service = service;
    }

    @Override
    public void onApplicationEvent(PlayStoppedEvent event) {

        String sensorId = event.getSource().toString();

        service.notifyObservers(sensorId);

    }
}

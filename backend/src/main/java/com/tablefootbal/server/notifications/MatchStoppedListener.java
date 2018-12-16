package com.tablefootbal.server.notifications;

import com.tablefootbal.server.events.PlayStoppedEvent;
import org.springframework.context.ApplicationListener;

public class MatchStoppedListener implements ApplicationListener<PlayStoppedEvent> {

    @Override
    public void onApplicationEvent(PlayStoppedEvent event) {

        String sensorId = event.getSource().toString();



    }
}

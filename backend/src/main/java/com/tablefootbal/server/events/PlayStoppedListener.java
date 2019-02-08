package com.tablefootbal.server.events;

import com.tablefootbal.server.service.SensorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class PlayStoppedListener implements ApplicationListener<PlayStoppedEvent> {

    final private
    SensorService sensorService;

    @Autowired
    public PlayStoppedListener(SensorService sensorService) {
        this.sensorService = sensorService;
    }

    @Override
    public void onApplicationEvent(PlayStoppedEvent event) {
        sensorService.setOccupied((String) event.getSource(), false);
    }
}

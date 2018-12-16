package com.tablefootbal.server.events;

import org.springframework.context.ApplicationEventPublisher;

public class PlayingTrackerTask implements Runnable {

    private ApplicationEventPublisher eventPublisher;
    private String sensorId;

    PlayingTrackerTask(ApplicationEventPublisher eventPublisher, String sensorId) {
        this.eventPublisher = eventPublisher;
        this.sensorId = sensorId;
    }

    @Override
    public void run() {
        eventPublisher.publishEvent(new PlayStoppedEvent(sensorId));
    }
}

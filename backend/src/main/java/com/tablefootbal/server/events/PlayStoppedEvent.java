package com.tablefootbal.server.events;

import org.springframework.context.ApplicationEvent;

public class PlayStoppedEvent extends ApplicationEvent {

    public PlayStoppedEvent(Object sensorId) {
        super(sensorId);
    }

    @Override
    public Object getSource() {
        return super.getSource();
    }
}

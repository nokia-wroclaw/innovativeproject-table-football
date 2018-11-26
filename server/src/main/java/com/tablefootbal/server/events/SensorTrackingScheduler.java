package com.tablefootbal.server.events;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;

@Component
@PropertySource("classpath:readings.properties")
public class SensorTrackingScheduler implements ApplicationListener<SensorOfflineEvent> {
    private static int ONE_SECOND_IN_MILIS = 1000;

    @Value("${readings.heartbeat_interval}")
    private int HEARTBEAT_INTERVAL;
    @Value("${readings.playing_interval}")
    private int PLAYING_INTERVAL;

    final private
    ApplicationEventPublisher eventPublisher;

    private Map<String, Future<?>> heartbeatTasks;
    private Map<String, Future<?>> playingTrackerTasks;

    final private
    ThreadPoolTaskScheduler scheduler;

    @Autowired
    public SensorTrackingScheduler(ApplicationEventPublisher eventPublisher,
                                   ThreadPoolTaskScheduler threadPoolTaskScheduler) {
        this.scheduler = threadPoolTaskScheduler;
        this.heartbeatTasks = new HashMap<>();
        this.playingTrackerTasks = new HashMap<>();
        this.eventPublisher = eventPublisher;
    }

    public void startTracking(String id) {
        Future task;
        if ((task = heartbeatTasks.get(id)) != null) {
            task.cancel(true);
        }
        if ((task = playingTrackerTasks.get(id)) != null) {
            task.cancel(true);
        }

        Runnable heartbeatTask = new HeartbeatTrackerTask(eventPublisher, id);

        long period = HEARTBEAT_INTERVAL * ONE_SECOND_IN_MILIS;

        ScheduledFuture heartbeatFuture = scheduler.scheduleAtFixedRate(heartbeatTask,
                new Date(System.currentTimeMillis() + period),
                period);

        heartbeatTasks.put(id, heartbeatFuture);

        Runnable activeTrackerTask = new PlayingTrackerTask(eventPublisher, id);

        period = PLAYING_INTERVAL * ONE_SECOND_IN_MILIS;

        ScheduledFuture playingTrackerTask = scheduler.schedule(activeTrackerTask,
                new Date(System.currentTimeMillis() + period));

        playingTrackerTasks.put(id, playingTrackerTask);
    }

    @Override
    public void onApplicationEvent(SensorOfflineEvent sensorOfflineEvent) {
        String sensorId = (String) sensorOfflineEvent.getSource();
        Future task = heartbeatTasks.get(sensorId);
        task.cancel(false);

        heartbeatTasks.remove(sensorId);
    }
}

package com.tablefootbal.server.events;

import lombok.Setter;
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
@Setter
@PropertySource("classpath:readings.properties")
public class SensorTrackingScheduler implements ApplicationListener<SensorOfflineEvent> {
    private static int ONE_SECOND_IN_MILIS = 1000;

//    @Value("${readings.heartbeat_interval}")
//    private int HEARTBEAT_INTERVAL;
//    @Value("${readings.playing_interval}")
//    private int PLAYING_INTERVAL;

    @Value("${readings.seconds_until_offline}")
    private int SECONDS_TILL_OFFLINE;

    final private
    ApplicationEventPublisher eventPublisher;

    //    private Map<String, Future<?>> heartbeatTasks;
//    private Map<String, Future<?>> playingTrackerTasks;
    private Map<String, Future<?>> offlineTrackerMap;

    final private
    ThreadPoolTaskScheduler scheduler;

    @Autowired
    public SensorTrackingScheduler(ApplicationEventPublisher eventPublisher,
                                   ThreadPoolTaskScheduler threadPoolTaskScheduler) {
        this.scheduler = threadPoolTaskScheduler;
//        this.heartbeatTasks = new HashMap<>();
//        this.playingTrackerTasks = new HashMap<>();
        this.offlineTrackerMap = new HashMap<>();
        this.eventPublisher = eventPublisher;
    }

    public void startTracking(String id) {
        Future task;
        if ((task = offlineTrackerMap.get(id)) != null) {
            task.cancel(true);
        }

        Runnable offlineTrackerTask = new OfflineTrackerTask(eventPublisher, id);

        long period = SECONDS_TILL_OFFLINE * ONE_SECOND_IN_MILIS;

        ScheduledFuture offlineTrackerFuture = scheduler.scheduleAtFixedRate(offlineTrackerTask,
                new Date(System.currentTimeMillis() + period),
                period);

        offlineTrackerMap.put(id, offlineTrackerFuture);
    }

    @Override
    public void onApplicationEvent(SensorOfflineEvent sensorOfflineEvent) {
        String sensorId = (String) sensorOfflineEvent.getSource();
        Future task = offlineTrackerMap.get(sensorId);
        task.cancel(false);
        offlineTrackerMap.remove(sensorId);
    }
}

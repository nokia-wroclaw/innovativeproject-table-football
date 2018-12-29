package com.tablefootbal.server.notifications.entity;


import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.HashSet;
import java.util.Set;

@RedisHash("matchobserverlist")
public class MatchObserverList {

    @Id
    @Getter
    private final String sensorID;

    @Getter
    private Set<MatchObserver> observers = new HashSet<>();

    public MatchObserverList(String sensorID){
        this.sensorID = sensorID;
    }

}

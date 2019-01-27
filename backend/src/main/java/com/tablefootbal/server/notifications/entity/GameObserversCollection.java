package com.tablefootbal.server.notifications.entity;


import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.HashSet;
import java.util.Set;

@RedisHash("matchobserverlist")
public class GameObserversCollection {

    @Id
    @Getter
    private final String sensorID;

    @Getter
    private HashSet<GameObserver> observers = new HashSet<>();

    public GameObserversCollection(String sensorID){
        this.sensorID = sensorID;
    }

}

package com.tablefootbal.server.notifications.entity;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.HashSet;
import java.util.Set;

@RedisHash("matchobserverlist")
public class MatchObserverList {

    @Id
    @Setter
    @Getter
    String sensorID;

    @Getter
    Set<MatchObserver> collection = new HashSet<>();


}

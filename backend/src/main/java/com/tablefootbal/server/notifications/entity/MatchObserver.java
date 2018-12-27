package com.tablefootbal.server.notifications.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.Date;

@Setter
@Getter
@RedisHash("match_observer")
public class MatchObserver {

    @Id
    private String sensorID;

    private TokenFCM tokenFCM;

    private Date registerDate;

    public MatchObserver() {}

}

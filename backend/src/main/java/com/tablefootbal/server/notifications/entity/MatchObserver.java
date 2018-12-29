package com.tablefootbal.server.notifications.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.Date;

@Setter
@Getter
public class MatchObserver {

    private final TokenFCM tokenFCM;

    public MatchObserver(TokenFCM tokenFCM) {
        this.tokenFCM = tokenFCM;
    }

}

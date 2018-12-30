package com.tablefootbal.server.notifications.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.Date;

@Setter
@Getter
@EqualsAndHashCode
public class GameObserver {

    private final TokenFCM tokenFCM;

    public GameObserver(TokenFCM tokenFCM) {
        this.tokenFCM = tokenFCM;
    }

}

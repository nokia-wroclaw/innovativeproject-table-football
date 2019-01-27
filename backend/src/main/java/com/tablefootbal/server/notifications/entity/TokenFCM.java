package com.tablefootbal.server.notifications.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.redis.core.RedisHash;

import java.util.Objects;

@Getter
@RedisHash("token_fcm")
@ToString
public class TokenFCM {

    private final String TOKEN;

    public TokenFCM(String TOKEN){
        this.TOKEN = TOKEN;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TokenFCM tokenFCM = (TokenFCM) o;
        return Objects.equals(TOKEN, tokenFCM.TOKEN);
    }

    @Override
    public int hashCode() {
        return Objects.hash(TOKEN);
    }
}

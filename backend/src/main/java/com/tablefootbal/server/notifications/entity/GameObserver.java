package com.tablefootbal.server.notifications.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Setter
@Getter
public class GameObserver {

    private final TokenFCM tokenFCM;

    public GameObserver(TokenFCM tokenFCM) {
        this.tokenFCM = tokenFCM;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameObserver that = (GameObserver) o;
        return Objects.equals(tokenFCM, that.tokenFCM);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tokenFCM);
    }

    @Override
    public String toString() {
        return tokenFCM.toString();
    }
}

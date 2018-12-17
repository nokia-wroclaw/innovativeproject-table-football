package com.tablefootbal.server.notifications.entity;

import com.tablefootbal.server.entity.Sensor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MatchObserver {

    private TokenFCM tokenFCM;

    private Sensor sensor;

    public MatchObserver(){}

    public MatchObserver(TokenFCM tokenFCM, Sensor sensor){
        this.tokenFCM = tokenFCM;
        this.sensor = sensor;
    }

}

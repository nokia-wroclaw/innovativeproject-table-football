package com.tablefootbal.server.notifications.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class GameObserverDto {

    public GameObserverDto(String fcm_token, String[] sensors_id){
        this.fcm_token = fcm_token;
        this.sensors_id = sensors_id.clone();
    }

    private String fcm_token;
    private String[] sensors_id;

}

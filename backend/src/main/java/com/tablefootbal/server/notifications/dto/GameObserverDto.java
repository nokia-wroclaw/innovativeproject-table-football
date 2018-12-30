package com.tablefootbal.server.notifications.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class GameObserverDto {

    private String fcm_token;
    private String[] sensors_id;

}

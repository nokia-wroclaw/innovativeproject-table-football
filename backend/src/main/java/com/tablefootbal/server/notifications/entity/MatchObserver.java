package com.tablefootbal.server.notifications.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.util.Date;

@Setter
@Getter
public class MatchObserver {

    @Id
    private String sensorID;

    private TokenFCM tokenFCM;

    private Date registerDate;

    public MatchObserver() {}

}

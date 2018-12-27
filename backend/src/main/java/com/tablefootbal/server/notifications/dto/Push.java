package com.tablefootbal.server.notifications.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class Push implements Serializable {

    private String to;

    private String priority;

    private Notification notification;


}



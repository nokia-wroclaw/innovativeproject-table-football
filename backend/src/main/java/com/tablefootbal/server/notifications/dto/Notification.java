package com.tablefootbal.server.notifications.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class Notification implements Serializable {

    private String title;

    private String body;

}

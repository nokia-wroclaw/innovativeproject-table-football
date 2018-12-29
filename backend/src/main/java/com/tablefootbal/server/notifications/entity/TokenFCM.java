package com.tablefootbal.server.notifications.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
public class TokenFCM {

    private final String TOKEN;

    public TokenFCM(String token){
        this.TOKEN = token;
    }

}

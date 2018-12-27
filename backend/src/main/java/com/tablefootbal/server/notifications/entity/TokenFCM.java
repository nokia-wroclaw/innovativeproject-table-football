package com.tablefootbal.server.notifications.entity;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TokenFCM {

    private String token;

    public TokenFCM(){}

    public TokenFCM(String token){
        this.token = token;
    }

}

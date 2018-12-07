package com.tablefootbal.server.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.Collection;

@Getter
@Setter
public class UserRole {
    String name;

    public static class USER_ROLES {
        public static final String ROLE_SENSOR = "ROLE_SENSOR";
        public static final String ROLE_ADMIN = "ROLE_ADMIN";
    }
}

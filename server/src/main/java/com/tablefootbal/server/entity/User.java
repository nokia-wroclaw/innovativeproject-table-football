package com.tablefootbal.server.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.Collection;

@Getter
@Setter
@RedisHash("users")
public class User implements Serializable
{
    @Id
	String username;
	
	String password;

	private Collection<UserRole> roles;
}

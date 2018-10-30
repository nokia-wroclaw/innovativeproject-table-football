package com.tablefootbal.server.repository;

import com.tablefootbal.server.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer>
{
	User findByUsername(String username);
}

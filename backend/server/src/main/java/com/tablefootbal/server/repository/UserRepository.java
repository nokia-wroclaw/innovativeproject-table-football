package com.tablefootbal.server.repository;

import com.tablefootbal.server.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, String> {
}

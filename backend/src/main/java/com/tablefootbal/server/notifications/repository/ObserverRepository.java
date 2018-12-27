package com.tablefootbal.server.notifications.repository;

import com.tablefootbal.server.notifications.entity.MatchObserver;
import org.springframework.data.repository.CrudRepository;

public interface ObserverRepository extends CrudRepository<MatchObserver, String> {
}

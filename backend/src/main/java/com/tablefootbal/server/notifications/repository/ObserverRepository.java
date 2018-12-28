package com.tablefootbal.server.notifications.repository;

import com.tablefootbal.server.notifications.entity.MatchObserver;
import com.tablefootbal.server.notifications.entity.MatchObserverList;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

public interface ObserverRepository extends CrudRepository<MatchObserverList, String> {
}

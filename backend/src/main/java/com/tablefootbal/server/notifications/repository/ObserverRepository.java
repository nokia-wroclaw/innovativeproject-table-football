package com.tablefootbal.server.notifications.repository;

import com.tablefootbal.server.notifications.entity.MatchObserversCollection;
import org.springframework.data.repository.CrudRepository;

public interface ObserverRepository extends CrudRepository<MatchObserversCollection, String> {
}

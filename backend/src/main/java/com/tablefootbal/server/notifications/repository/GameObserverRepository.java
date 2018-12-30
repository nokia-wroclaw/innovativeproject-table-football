package com.tablefootbal.server.notifications.repository;

import com.tablefootbal.server.notifications.entity.GameObserversCollection;
import org.springframework.data.repository.CrudRepository;

public interface GameObserverRepository extends CrudRepository<GameObserversCollection, String> {
}

package com.tablefootbal.server.notifications.repository;

import com.tablefootbal.server.entity.Sensor;
import com.tablefootbal.server.notifications.entity.ObserversList;
import org.springframework.data.repository.CrudRepository;

public interface ObserversListRepository extends CrudRepository<Sensor, ObserversList>{
}

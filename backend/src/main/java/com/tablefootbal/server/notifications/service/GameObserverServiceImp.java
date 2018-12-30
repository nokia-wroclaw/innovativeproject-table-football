package com.tablefootbal.server.notifications.service;

import com.tablefootbal.server.notifications.entity.GameObserver;
import com.tablefootbal.server.notifications.entity.GameObserversCollection;
import com.tablefootbal.server.notifications.repository.GameObserverRepository;
import com.tablefootbal.server.service.SensorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class GameObserverServiceImp implements GameObserverService {

    private GameObserverRepository observerRepository;

    private  SensorService sensorService;

    @Autowired
    public GameObserverServiceImp(SensorService sensorService, GameObserverRepository observerRepository) {
       this.observerRepository = observerRepository;
       this.sensorService = sensorService;
    }

    @Override
    public void register(GameObserver observer, String[] sensor_IDs) {

        GameObserversCollection list;

        for(String id: sensor_IDs){
            Optional<GameObserversCollection> optional = observerRepository.findById(id);
            if(optional.isPresent()){
                list = optional.get();
                list.getObservers().add(observer);
            }else{
                list = new GameObserversCollection(id);
                list.getObservers().add(observer);
            }

            observerRepository.save(list);
            log.info("Registered an observer" + list.toString());
        }

    }

}

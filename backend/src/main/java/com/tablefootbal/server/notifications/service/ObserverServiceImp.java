package com.tablefootbal.server.notifications.service;

import com.tablefootbal.server.notifications.entity.MatchObserver;
import com.tablefootbal.server.notifications.entity.MatchObserverList;
import com.tablefootbal.server.notifications.repository.ObserverRepository;
import com.tablefootbal.server.service.SensorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ObserverServiceImp implements ObserverService {

    private  ObserverRepository observerRepository;

    private  SensorService sensorService;

    @Autowired
    public ObserverServiceImp(SensorService sensorService, ObserverRepository observerRepository) {
       this.observerRepository = observerRepository;
       this.sensorService = sensorService;
    }

    @Override
    public void register(MatchObserver observer, String[] sensor_IDs) {

        MatchObserverList list;

        for(String id: sensor_IDs){
            Optional<MatchObserverList> optional = observerRepository.findById(id);
            if(optional.isPresent()){
                list = optional.get();
                list.getObservers().add(observer);
            }else{
                list = new MatchObserverList(id);
                list.getObservers().add(observer);
            }

            observerRepository.save(list);
        }

    }

}

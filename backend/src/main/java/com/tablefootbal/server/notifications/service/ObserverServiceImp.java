package com.tablefootbal.server.notifications.service;

import com.tablefootbal.server.notifications.entity.MatchObserver;
import com.tablefootbal.server.service.SensorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ObserverServiceImp implements ObserverService {

    //private final ObserverRepository observerRepository;

    private final SensorService sensorService;

    @Autowired
    public ObserverServiceImp(SensorService sensorService) {
       // this.observerRepository = observerRepository;
        this.sensorService = sensorService;
    }

    @Override
    public void register(MatchObserver observer) {
        //observerRepository.save(observer);
    }

    @Override
    public void register(List<MatchObserver> listToRegister) {
        for(MatchObserver observer: listToRegister);
            //observerRepository.save(observer);
    }
}

package com.tablefootbal.server.notifications.service;

import com.tablefootbal.server.entity.Sensor;
import com.tablefootbal.server.notifications.dto.Notification;
import com.tablefootbal.server.notifications.dto.Push;
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

    private SensorService sensorService;

    private NotificationService notificationService;

    @Autowired
    public GameObserverServiceImp(SensorService sensorService, GameObserverRepository observerRepository, NotificationService notificationService) {
       this.observerRepository = observerRepository;
       this.sensorService = sensorService;
       this.notificationService = notificationService;
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

            log.info("ObserverService: ","Registered an observer for sensor ID: " + list.getSensorID());
            StringBuilder stringBuilder = new StringBuilder("Actually registered for: " + list.getSensorID() + ":");
            for(GameObserver g: list.getObservers()){
                stringBuilder.append(g.toString() +" ");
            }
            log.info("ObserverService: ",stringBuilder.toString());
        }

    }

    @Override
    public int notifyObservers(String sensorID){
        Optional<GameObserversCollection> optional = observerRepository.findById(sensorID);

        if(optional.isPresent()){
            Optional<Sensor> sensorOptional = sensorService.findById(sensorID);
            String floorAndRoom = "Table on X floor in X room is available";

            if(sensorOptional.isPresent()){
                Sensor sensor = sensorOptional.get();
                floorAndRoom = "Table on " + sensor.getFloor() + " floor in room " + sensor.getRoom() + " is available!";
            }

            Notification notification = new Notification();
            notification.setBody("Hurry up!");
            notification.setTitle(floorAndRoom);

            Push push = new Push();
            push.setPriority("HIGH");
            push.setNotification(notification);

            GameObserversCollection collection = optional.get();
            for(GameObserver observer: collection.getObservers()){
                push.setTo(observer.getTokenFCM().getTOKEN());
                notificationService.sendNotification(push);
            }

            return collection.getObservers().size();
        }

        return 0;
    }

}

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

        for (String id : sensor_IDs) {
            Optional<GameObserversCollection> optional = observerRepository.findById(id);
            if (optional.isPresent()) {
                list = optional.get();
                list.getObservers().add(observer);
            } else {
                list = new GameObserversCollection(id);
                list.getObservers().add(observer);
            }

            observerRepository.save(list);
        }

    }

    @Override
    public int notifyObservers(String sensorID) {
        Optional<GameObserversCollection> optional = observerRepository.findById(sensorID);

        if (optional.isPresent()) {
            Optional<Sensor> sensorOptional = sensorService.findById(sensorID);
            String floorAndRoom = "Table on X floor in X room is available";

            //TODO move notification body and title construction to mobile app
            if (sensorOptional.isPresent()) {
                Sensor sensor = sensorOptional.get();
                floorAndRoom = "Table in " + sensor.getRoom() + ". room on " + sensor.getFloor() + ". floor is available!";
            }

            Notification notification = new Notification();
            notification.setTitle("Hurry up!");
            notification.setBody(floorAndRoom);

            Push push = new Push();
            push.setPriority("HIGH");
            push.setNotification(notification);

            GameObserversCollection collection = optional.get();
            for (GameObserver observer : collection.getObservers()) {
                push.setTo(observer.getTokenFCM().getTOKEN());
                log.info(notificationService.sendNotification(push).toString());
            }

            observerRepository.deleteById(sensorID);

            return collection.getObservers().size();
        }

        return 0;
    }

}

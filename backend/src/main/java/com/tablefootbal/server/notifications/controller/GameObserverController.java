package com.tablefootbal.server.notifications.controller;

import com.tablefootbal.server.notifications.dto.GameObserverDto;
import com.tablefootbal.server.notifications.entity.TokenFCM;
import com.tablefootbal.server.notifications.entity.GameObserver;
import com.tablefootbal.server.notifications.service.GameObserverService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/register")
@Slf4j
public class GameObserverController {

    final private GameObserverService gameObserverService;


    @Autowired
    public GameObserverController(GameObserverService gameObserverService){
        this.gameObserverService = gameObserverService;
    }

    @PostMapping("/")
    public void registerDevice(@RequestBody GameObserverDto gameObserverDto){

        log.info("Received register request from" + gameObserverDto.getFcm_token().substring(0,5) + "...");

        TokenFCM token = new TokenFCM(gameObserverDto.getFcm_token());
        GameObserver observer = new GameObserver(token);

        gameObserverService.register(observer, gameObserverDto.getSensors_id());

    }

}

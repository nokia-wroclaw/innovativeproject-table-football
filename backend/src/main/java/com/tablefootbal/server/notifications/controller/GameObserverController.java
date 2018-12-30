package com.tablefootbal.server.notifications.controller;

import com.tablefootbal.server.notifications.dto.GameObserverDto;
import com.tablefootbal.server.notifications.entity.TokenFCM;
import com.tablefootbal.server.notifications.entity.GameObserver;
import com.tablefootbal.server.notifications.service.GameObserverService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

@RestController
@RequestMapping("/register")
@Slf4j
public class GameObserverController {

    private DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    final private GameObserverService gameObserverService;

    final private MessageSource messageSource;


    @Autowired
    public GameObserverController(GameObserverService gameObserverService, MessageSource messageSource){
        this.gameObserverService = gameObserverService;
        this.messageSource = messageSource;
    }

    @PostMapping("/")
    public void registerDevice(@RequestBody GameObserverDto gameObserverDto){

        TokenFCM token = new TokenFCM(gameObserverDto.getFcm_token());
        GameObserver observer = new GameObserver(token);


        gameObserverService.register(observer, gameObserverDto.getSensors_id());



    }
}

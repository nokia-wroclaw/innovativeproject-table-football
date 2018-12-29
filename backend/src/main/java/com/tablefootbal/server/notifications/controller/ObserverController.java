package com.tablefootbal.server.notifications.controller;

import com.tablefootbal.server.notifications.dto.MatchObserverDto;
import com.tablefootbal.server.notifications.entity.TokenFCM;
import com.tablefootbal.server.notifications.entity.MatchObserver;
import com.tablefootbal.server.notifications.service.ObserverService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/register")
@Slf4j
public class ObserverController {

    private DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    final private ObserverService observerService;

    final private MessageSource messageSource;


    @Autowired
    public ObserverController(ObserverService observerService, MessageSource messageSource){
        this.observerService = observerService;
        this.messageSource = messageSource;
    }

    @PostMapping("/")
    public void registerDevice(@RequestBody MatchObserverDto matchObserverDto){

        TokenFCM token = new TokenFCM(matchObserverDto.getFcm_token());
        MatchObserver observer = new MatchObserver(token);


        observerService.register(observer,matchObserverDto.getSensors_id());



    }
}

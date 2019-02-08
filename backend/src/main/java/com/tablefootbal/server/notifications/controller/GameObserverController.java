package com.tablefootbal.server.notifications.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tablefootbal.server.exceptions.customExceptions.InvalidSensorDataException;
import com.tablefootbal.server.notifications.dto.GameObserverDto;
import com.tablefootbal.server.notifications.entity.GameObserver;
import com.tablefootbal.server.notifications.entity.TokenFCM;
import com.tablefootbal.server.notifications.exception.InvalidRegisterDataException;
import com.tablefootbal.server.notifications.service.GameObserverService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Locale;

@RestController
@RequestMapping("/register")
@Slf4j
public class GameObserverController {

    final private GameObserverService gameObserverService;

    final private MessageSource messageSource;

    @Autowired
    public GameObserverController(GameObserverService gameObserverService, MessageSource messageSource) {
        this.gameObserverService = gameObserverService;
        this.messageSource = messageSource;
    }

    // TODO add validator for gameObserverDto
    @PostMapping
    public ResponseEntity<?> registerDevice(@RequestBody @Valid GameObserverDto gameObserverDto,
                                            BindingResult result) {

        if (result.hasErrors()) {
            StringBuilder stringBuilder = new StringBuilder("Binding Exceptions: ");
            for (ObjectError error : result.getAllErrors()) {
                stringBuilder.append("\n");
                stringBuilder.append(
                        messageSource.getMessage(error.getCode(), null, Locale.getDefault()));
            }
            throw new InvalidRegisterDataException(stringBuilder.toString());
        }

        TokenFCM token = new TokenFCM(gameObserverDto.getFcm_token());
        GameObserver observer = new GameObserver(token);

        gameObserverService.register(observer, gameObserverDto.getSensors_id());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ExceptionHandler(InvalidRegisterDataException.class)
    public ResponseEntity<String> handleInvalidSensorData(InvalidSensorDataException exception) {
        log.error(exception.getMessage());
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(JsonProcessingException.class)
    public ResponseEntity<String> handleJsonError(JsonProcessingException exception) {
        log.error(exception.getMessage());
        return new ResponseEntity<>(
                messageSource.getMessage("error.invalid_json", null, Locale.getDefault()),
                HttpStatus.BAD_REQUEST);
    }

}

package com.tablefootbal.server.notification;

import com.tablefootbal.server.notifications.controller.GameObserverController;
import com.tablefootbal.server.notifications.dto.GameObserverDto;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.junit.Assert;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest()
public class GameObserverControllerTest {

    @Autowired
    private GameObserverController controller;

    private int port=  8080;

    @Test
    public void contextLoads(){
        Assert.assertNotNull(controller);
    }
//
//    @Test
//    public void givenRequestToRegister_shouldReturnHTTP_OK(){
//        TestRestTemplate restTemplate = new TestRestTemplate();
//        String url = "http://localhost"+port+"/register";
//
//        GameObserverDto goDto = new GameObserverDto();
//        goDto.setFcm_token("token1234124121412");
//        goDto.setSensors_id( new String[]{"123445","12315555"});
//
//        HttpEntity<GameObserverDto> request = new HttpEntity<>(goDto);
//        ResponseEntity<?> response = restTemplate
//                .exchange(url, HttpMethod.POST, request, String.class);
//
//        assertThat(response.getStatusCode(), is(HttpStatus.OK));
//
//        //TODO check where the problem is...
//
//    }


}

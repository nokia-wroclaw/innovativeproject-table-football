package com.tablefootbal.server.notification;

import com.tablefootbal.server.notifications.entity.MatchObserver;
import com.tablefootbal.server.notifications.entity.MatchObserverList;
import com.tablefootbal.server.notifications.entity.TokenFCM;
import com.tablefootbal.server.notifications.repository.ObserverRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class MatchObserverRepositoryTest {

    @Autowired
    public ObserverRepository repository;
    
    private List<MatchObserver> addedObservers = new ArrayList<>();


    @Before
    public void populateData(){

    }

    @Test
    public void shouldBeTwoObjectsInRepo(){
        MatchObserverList l1 = new MatchObserverList();
        MatchObserverList l2 = new MatchObserverList();

        l1.setSensorID("123");
        l2.setSensorID("223");

        repository.save(l1);
        repository.save(l2);


        Assert.assertEquals(2,repository.count());
    }

    @After
    public void cleanUp(){

    }

}

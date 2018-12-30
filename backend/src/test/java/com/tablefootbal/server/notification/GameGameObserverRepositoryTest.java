package com.tablefootbal.server.notification;

import com.tablefootbal.server.notifications.entity.GameObserver;
import com.tablefootbal.server.notifications.entity.GameObserversCollection;
import com.tablefootbal.server.notifications.repository.GameObserverRepository;
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
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GameGameObserverRepositoryTest {

    @Autowired
    public GameObserverRepository repository;
    
    private List<GameObserver> addedObservers = new ArrayList<>();


    @Before
    public void populateData(){

    }

    @Test
    public void shouldBeTwoObjectsInRepo(){
        GameObserversCollection l1 = new GameObserversCollection("123");
        GameObserversCollection l2 = new GameObserversCollection("223");

        repository.save(l1);
        repository.save(l2);

        Assert.assertEquals(2,repository.count());
    }

    @Test
    public void shouldFindById(){
        String id = "123";
        GameObserversCollection l1 = new GameObserversCollection(id);
        repository.save(l1);

        Optional<GameObserversCollection> optional = repository.findById(id);
        Assert.assertTrue(optional.isPresent());
    }

    @After
    public void cleanUp(){
        repository.deleteAll();
    }

}

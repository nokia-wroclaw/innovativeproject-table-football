package com.tablefootbal.server.notification;

import com.tablefootbal.server.notifications.entity.MatchObserver;
import com.tablefootbal.server.notifications.entity.MatchObserverList;
import com.tablefootbal.server.notifications.repository.ObserverRepository;
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
public class MatchObserverRepositoryTest {

    @Autowired
    public ObserverRepository repository;
    
    private List<MatchObserver> addedObservers = new ArrayList<>();


    @Before
    public void populateData(){

    }

    @Test
    public void shouldBeTwoObjectsInRepo(){
        MatchObserverList l1 = new MatchObserverList("123");
        MatchObserverList l2 = new MatchObserverList("223");

        repository.save(l1);
        repository.save(l2);

        Assert.assertEquals(2,repository.count());
    }

    @Test
    public void shouldFindById(){
        String id = "123";
        MatchObserverList l1 = new MatchObserverList(id);
        repository.save(l1);

        Optional<MatchObserverList> optional = repository.findById(id);
        Assert.assertTrue(optional.isPresent());
    }

    @After
    public void cleanUp(){
        repository.deleteAll();
    }

}

package com.tablefootbal.server.notification;

import com.tablefootbal.server.notifications.entity.MatchObserver;
import com.tablefootbal.server.notifications.entity.TokenFCM;
import com.tablefootbal.server.notifications.repository.ObserverRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MatchObserverRepositoryTest {

    @Autowired
    private ObserverRepository repository;

    private List<MatchObserver> addedObservers = new ArrayList<>();



    @Before
    public void populateData(){
        MatchObserver o1 = new MatchObserver();
        o1.setSensorID("08:3F:33:33:3D:30");
        TokenFCM t1 = new TokenFCM();
        t1.setToken("123");
        o1.setTokenFCM(t1);

        MatchObserver o2 = new MatchObserver();
        o2.setSensorID("08:3F:33:33:3D:30");
        TokenFCM t2 = new TokenFCM();
        t2.setToken("AAA");
        o2.setTokenFCM(t2);

        addedObservers.add(o1);
        addedObservers.add(o2);
    }

    @Test
    public void shouldBeTwoObjectsInRepo(){
        for(MatchObserver o: addedObservers)
            repository.save(o);

        List<MatchObserver> actual = new ArrayList<>();
        Iterator<MatchObserver> iter = repository.findAll().iterator();
        while(iter.hasNext())
            actual.add(iter.next());

        Assert.assertEquals(addedObservers.size(),actual.size());
    }

}

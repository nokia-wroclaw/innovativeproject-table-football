package com.tablefootbal.server.notification;

import com.tablefootbal.server.notifications.entity.MatchObserver;
import com.tablefootbal.server.notifications.entity.TokenFCM;
import com.tablefootbal.server.notifications.repository.ObserverRepository;
import com.tablefootbal.server.notifications.service.ObserverServiceImp;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class MatchObserverServiceTest {

    @Mock
    ObserverRepository repository;

    @InjectMocks
    ObserverServiceImp service;

    MatchObserver o1;
    String[] o1_list;

    MatchObserver o2;
    String[] o2_list;

    @Before
    public void setUp() {
        o1 = new MatchObserver(new TokenFCM("aav"));
        o1_list = new String[]{"111", "222", "333"};

        o2 = new MatchObserver(new TokenFCM("bbb"));
        o2_list = new String[]{"222", "333", "444"};

    }

    @Test
    public void testRegisterObservers(){
        service.register(o1,o1_list);
        service.register(o2,o2_list);

        Assert.assertEquals(2, repository.count());
    }

    @After
    public void cleanUp(){
        repository.deleteAll();
    }
}

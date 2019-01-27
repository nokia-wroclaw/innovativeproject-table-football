package com.tablefootbal.server.notification;

import com.tablefootbal.server.notifications.entity.GameObserver;
import com.tablefootbal.server.notifications.entity.GameObserversCollection;
import com.tablefootbal.server.notifications.entity.TokenFCM;
import com.tablefootbal.server.notifications.repository.GameObserverRepository;
import com.tablefootbal.server.notifications.service.GameObserverServiceImp;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;

@RunWith(SpringRunner.class)
public class GameGameObserverServiceTest {

    @Mock
    GameObserverRepository repository;

    @InjectMocks
    GameObserverServiceImp service;

    GameObserver o1;
    String[] o1_list;

    GameObserver o2;
    String[] o2_list;

    @Before
    public void setUp() {
        o1 = new GameObserver(new TokenFCM("aav"));
        o1_list = new String[]{"111", "222", "333"};

        o2 = new GameObserver(new TokenFCM("bbb"));
        o2_list = new String[]{"222", "333", "444"};

    }

    @Test
    public void givenObserverCalledForTwoSensors_thenSaveCalledTwoTimes() {
        //Doesn't really matter what will be returned
        Mockito.when(repository.save(Mockito.any(GameObserversCollection.class))).thenReturn(new GameObserversCollection("id"));

        Mockito.when(repository.findById(Mockito.anyString())).thenReturn(Optional.empty());
        String[] id = {"11:11:11:11:11:11", "22:22:22:22:22:22"};
        GameObserver observer = new GameObserver(new TokenFCM("TOKEN"));
        service.register(observer, id);
        Mockito.verify(repository, Mockito.times(2)).save(Mockito.any(GameObserversCollection.class));

    }

    @Test
    public void givenObserversListAlreadyInRepository_thenObserverAddedToTheLists() {
        //Doesn't really matter what will be returned
        Mockito.when(repository.save(Mockito.any(GameObserversCollection.class))).thenReturn(new GameObserversCollection("id"));

        String id1 = "11:11:11:11:11:11";
        String id2 = "22:22:22:22:22:22";
        String[] id = {id1, id2};

        GameObserversCollection list1 = new GameObserversCollection(id1);
        GameObserversCollection list2 = new GameObserversCollection(id2);
        List expected = asList(list1, list2);

        ArgumentCaptor<GameObserversCollection> listCaptor = ArgumentCaptor.forClass(GameObserversCollection.class);

        Mockito.when(repository.findById(id1)).thenReturn(Optional.of(list1));
        Mockito.when(repository.findById(id2)).thenReturn(Optional.of(list2));

        GameObserver observer = new GameObserver(new TokenFCM("TOKEN"));

        service.register(observer, id);

        Mockito.verify(repository, Mockito.times(2)).save(listCaptor.capture());
        Assert.assertEquals(expected, listCaptor.getAllValues());

        Assert.assertTrue(list1.getObservers().contains(observer));
        Assert.assertTrue(list2.getObservers().contains(observer));
    }


    @After
    public void cleanUp() {
        repository.deleteAll();
    }
}

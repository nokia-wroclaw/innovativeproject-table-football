package com.tablefootbal.server.notification;

import com.tablefootbal.server.notifications.entity.MatchObserver;
import com.tablefootbal.server.notifications.entity.MatchObserverList;
import com.tablefootbal.server.notifications.entity.TokenFCM;
import com.tablefootbal.server.notifications.repository.ObserverRepository;
import com.tablefootbal.server.notifications.service.ObserverServiceImp;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;

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

//    @Test
//    public void testRegisterObservers() {
//        service.register(o1, o1_list);
//        service.register(o2, o2_list);
//
//        Assert.assertEquals(2, repository.count());
//    }

    @Test
    public void givenObserverCalledForTwoSensors_thenSaveCalledTwoTimes()
    {
        //Doesn't really matter what will be returned
        Mockito.when(repository.save(Mockito.any(MatchObserverList.class))).thenReturn(new MatchObserverList("id"));

        Mockito.when(repository.findById(Mockito.anyString())).thenReturn(Optional.empty());
        String [] id = {"11:11:11:11:11:11","22:22:22:22:22:22"};
        MatchObserver observer = new MatchObserver(new TokenFCM("TOKEN"));
        service.register(observer,id);
        Mockito.verify(repository,Mockito.times(2)).save(Mockito.any(MatchObserverList.class));

    }

    @Test
    public void givenObserversListAlreadyInRepository_thenObserverAddedToTheLists()
    {
        //Doesn't really matter what will be returned
        Mockito.when(repository.save(Mockito.any(MatchObserverList.class))).thenReturn(new MatchObserverList("id"));

        String id1 = "11:11:11:11:11:11";
                String id2 = "22:22:22:22:22:22";
        String [] id = {id1,id2};

        MatchObserverList list1 = new MatchObserverList(id1);
        MatchObserverList list2 = new MatchObserverList(id2);
        List expected = asList(list1,list2);

        ArgumentCaptor<MatchObserverList> listCaptor = ArgumentCaptor.forClass(MatchObserverList.class);

        Mockito.when(repository.findById(id1)).thenReturn(Optional.of(list1));
        Mockito.when(repository.findById(id2)).thenReturn(Optional.of(list2));

        MatchObserver observer = new MatchObserver(new TokenFCM("TOKEN"));

        service.register(observer,id);

        Mockito.verify(repository,Mockito.times(2)).save(listCaptor.capture());
        Assert.assertEquals(expected,listCaptor.getAllValues());

        Assert.assertTrue(list1.getObservers().contains(observer));
        Assert.assertTrue(list2.getObservers().contains(observer));
    }

    @After
    public void cleanUp(){
        repository.deleteAll();
    }
}

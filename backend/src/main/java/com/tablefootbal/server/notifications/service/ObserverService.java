package com.tablefootbal.server.notifications.service;

import com.tablefootbal.server.notifications.entity.MatchObserver;

import java.util.List;

public interface ObserverService {

    void register(MatchObserver observer);
    void register(List<MatchObserver> listToRegister);
}

package com.tablefootbal.server.notifications.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class ObserversList implements Serializable {

    private Set<MatchObserver> set;

    public ObserversList(){
        this.set = new HashSet<>();
    }

    public Set<MatchObserver> getSet(){
        return this.set;
    }

    public void clean(){
        this.set = new HashSet<>();
    }
}

package com.craft.biddingSystem.services;

import com.craft.biddingSystem.models.Event;

import java.util.List;


public interface ObserverService {
    void updateObserver(List<String> ids, Event event);
}

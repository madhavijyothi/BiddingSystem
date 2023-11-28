package com.craft.biddingSystem.services;

import com.craft.biddingSystem.enums.ObserverType;
import com.craft.biddingSystem.models.Event;

import java.util.List;


public interface PublisherService {
    void updateObservers(List<String> observerIds, ObserverType type, Event event);

}

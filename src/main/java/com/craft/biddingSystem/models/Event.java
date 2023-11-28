package com.craft.biddingSystem.models;

import com.craft.biddingSystem.enums.PublisherEventType;
import lombok.Data;

import java.util.Date;

@Data
public class Event {
    private PublisherEventType eventType;
    private String message;
    private Date dateTime;
}

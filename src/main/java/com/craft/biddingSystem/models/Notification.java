package com.craft.biddingSystem.models;

import lombok.Data;

import java.util.Date;

@Data
public class Notification {
    private String message;
    private Date dateTime;

    public Notification(String message, Date dateTime) {
        this.message = message;
        this.dateTime = dateTime;
    }
}

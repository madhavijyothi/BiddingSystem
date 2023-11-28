package com.craft.biddingSystem.models;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Data
@Table(name = "bid_subscribe")
public class BidSubscribe {

    @Id
    @Column(name = "bid_subscribe_id")
    private String bidSubscribeId;
    @Column(name = "auction_id")
    private String auctionId;
    @Column(name = "buyer_id")
    private String buyerId;

    @Column(name = "bid_price")
    private double bidPrice;

    @Column(name = "modified_date", columnDefinition = "TIMESTAMP")
    private Date modifiedDate;

    @Column(name = "isSubscribed")
    private Integer isSubscribed;

    public BidSubscribe(String bidSubscribeId, String auctionId, String buyerId, double bidPrice, Date modifiedDate) {
        this.bidSubscribeId = bidSubscribeId;
        this.auctionId = auctionId;
        this.buyerId = buyerId;
        this.bidPrice = bidPrice;
        this.modifiedDate = modifiedDate;
    }

    public BidSubscribe() {
    }


}

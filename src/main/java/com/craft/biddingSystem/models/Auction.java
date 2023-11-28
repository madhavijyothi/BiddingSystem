package com.craft.biddingSystem.models;

import com.craft.biddingSystem.enums.AuctionProductState;
import com.craft.biddingSystem.enums.AuctionState;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;


@Entity
@Data
@Table(name = "auctions")
public class Auction extends Publisher {

    @Id
    @Column(name = "auction_id")
    private String auctionId;

    @Column(name = "seller_id")
    private String sellerId;

    @Enumerated(EnumType.STRING)
    @Column(name = "auction_state")
    private AuctionState auctionState;

    @Column(name = "start_date" , columnDefinition = "TIMESTAMP")
    private LocalDateTime startDate;

    @Column(name = "end_date" , columnDefinition = "TIMESTAMP")
    private LocalDateTime endDate;

    @Column(name = "base_price")
    Double basePrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "auction_product_state")
    AuctionProductState auctionProductState;

    @Column(name = "current_bid")
    private Double currentBid;


    @Column(name = "current_winning_buyer_id")
    private String currentWinningBuyerId;

    @Column(name = "created_date" , columnDefinition = "TIMESTAMP")
    private Date createdDate;

    @Column(name = "modified_date" , columnDefinition = "TIMESTAMP")
    private Date modifiedDate;

}

package com.craft.biddingSystem.response;

import com.craft.biddingSystem.enums.AuctionProductState;
import com.craft.biddingSystem.enums.AuctionState;
import lombok.Data;

@Data
public class AuctionDetailResponse {

    private String auctionId;
    private AuctionState auctionState;
    private AuctionProductState auctionProductState;
    private Double basePrice;
    private Double currentBid;
    private String productId;
    private String productName;
    private String productDescription;
    private String category;
}

package com.craft.biddingSystem.request;

import com.craft.biddingSystem.enums.AuctionState;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class AuctionRequest {
    private String auctionId;
    @NotNull
    private String sellerId;
    @NotNull
    private String category;
    private String productName;
    private String productDescription;
    @NotNull
    private double basePrice;
    @NotNull
    private AuctionState auctionState;
    @NotNull
    private LocalDateTime startDate;
    @NotNull
    private LocalDateTime endDate;
}

package com.craft.biddingSystem.services.detailView;

import com.craft.biddingSystem.response.AuctionDetailResponse;
import org.springframework.stereotype.Service;

@Service
public abstract class AuctionDetails {

    public abstract AuctionDetailResponse getAuctionDetails(String userId, String auctionId);
}

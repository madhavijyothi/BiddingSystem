package com.craft.biddingSystem.repository;

import com.craft.biddingSystem.models.BidSubscribe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BidSubscribeRepository extends JpaRepository<BidSubscribe, String> {

    BidSubscribe findByBidSubscribeId(String subscriptionId);

    List<BidSubscribe> findByBuyerId(String buyerId);

    List<BidSubscribe> findByAuctionId(String auctionId);
    List<BidSubscribe> findByAuctionIdAndIsSubscribed(String auctionId, Integer isSubscribed);



}

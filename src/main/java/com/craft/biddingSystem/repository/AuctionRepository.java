package com.craft.biddingSystem.repository;

import com.craft.biddingSystem.enums.AuctionState;
import com.craft.biddingSystem.models.Auction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface AuctionRepository extends JpaRepository<Auction, String> {
    Auction findByAuctionId(String auctionId);

    List<Auction> findBySellerId(String sellerId);


    @Query("SELECT a FROM Auction a WHERE  a.sellerId = :sellerId AND a.auctionState = :auctionState")
    List<Auction> findBySellerIdAndAuctionState(@Param("sellerId") String sellerId, @Param("auctionState") AuctionState auctionState);

    @Query("SELECT a FROM Auction a WHERE a.auctionState = :auctionState AND a.startDate = :startDate")
    List<Auction> findByAuctionStateAndStartDate(@Param("auctionState") AuctionState auctionState, @Param("startDate") LocalDateTime startDate);

    @Query("SELECT a FROM Auction a WHERE a.auctionState = :auctionState AND a.endDate = :endDate")
    List<Auction> findByAuctionStateAndEndDate(@Param("auctionState") AuctionState auctionState, @Param("endDate") LocalDateTime endDate);

}

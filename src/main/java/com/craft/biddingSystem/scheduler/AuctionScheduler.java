package com.craft.biddingSystem.scheduler;

import com.craft.biddingSystem.models.Auction;
import com.craft.biddingSystem.repository.AuctionRepository;
import com.craft.biddingSystem.enums.AuctionState;
import com.craft.biddingSystem.services.AuctionService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Service
public class AuctionScheduler {

    private final AuctionService auctionService;
    private final AuctionRepository auctionRepository;

    public AuctionScheduler(AuctionService auctionService, AuctionRepository auctionRepository) {
        this.auctionService = auctionService;
        this.auctionRepository = auctionRepository;
    }

    @Scheduled(cron = "0 48  15 * * *")
    public void startAuction() {


        LocalDateTime startDate = LocalDateTime.parse("2023-11-25T15:48:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        List<Auction> auctionList = auctionRepository.findByAuctionStateAndStartDate(AuctionState.SAVED, startDate);
        for (Auction auction : auctionList) {
            System.out.println("auction starting : " + auction.getAuctionId());
            auctionService.startAuction(auction, auction.getAuctionId());
        }

    }

    @Scheduled(cron = "0 00 16 * * *")
    public void endAuction() {


        LocalDateTime endDate = LocalDateTime.parse("2023-11-25T16:00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        List<Auction> auctionList = auctionRepository.findByAuctionStateAndEndDate(AuctionState.STARTED, endDate);

        for (Auction auction : auctionList) {
            System.out.println("auctionEnd process started : " + auction.getAuctionId());
            auctionService.endAuction(auction);
        }
        System.out.println("Scheduler is running");

    }
}

package com.craft.BiddingSystemTest.scheduler;

import com.craft.biddingSystem.BiddingServiceApplication;
import com.craft.biddingSystem.models.Auction;
import com.craft.biddingSystem.repository.AuctionRepository;
import com.craft.biddingSystem.scheduler.AuctionScheduler;
import com.craft.biddingSystem.services.AuctionService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.*;

@SpringBootTest(classes = BiddingServiceApplication.class)
public class AuctionSchedulerTest {

    @Mock
    private AuctionService auctionService;

    @Mock
    private AuctionRepository auctionRepository;

    @InjectMocks
    private AuctionScheduler auctionScheduler;


    @Test
    public void testStartAuction() {
        // Mock data
        Date currentDate = new Date();
        List<Auction> auctionList = new ArrayList<>();
        Auction auction = new Auction();
        auctionList.add(auction);


        when(auctionRepository.findByAuctionStateAndStartDate(any(), any())).thenReturn(auctionList);

        doNothing().when(auctionService).startAuction(any(), anyString());

        auctionScheduler.startAuction();

        verify(auctionRepository, times(1)).findByAuctionStateAndStartDate(any(), any());
        verify(auctionService, times(1)).startAuction(auction, auction.getAuctionId());
    }

    @Test
    public void testEndAuction() {
        Date currentDate = new Date();
        List<Auction> auctionList = new ArrayList<>();
        Auction auction = new Auction();
        auctionList.add(auction);
        when(auctionRepository.findByAuctionStateAndEndDate(any(), any())).thenReturn(auctionList);
        doNothing().when(auctionService).endAuction(any());
        auctionScheduler.endAuction();

        verify(auctionService, times(1)).endAuction(auction);
    }
}


package com.craft.BiddingSystemTest.service.detailViewTest;

import com.craft.biddingSystem.BiddingServiceApplication;
import com.craft.biddingSystem.enums.AuctionProductState;
import com.craft.biddingSystem.enums.AuctionState;
import com.craft.biddingSystem.exception.BadRequestException;
import com.craft.biddingSystem.models.Auction;
import com.craft.biddingSystem.models.BidSubscribe;
import com.craft.biddingSystem.models.Product;
import com.craft.biddingSystem.repository.AuctionRepository;
import com.craft.biddingSystem.repository.BidSubscribeRepository;
import com.craft.biddingSystem.repository.ProductRepository;
import com.craft.biddingSystem.response.AuctionDetailResponse;
import com.craft.biddingSystem.services.detailView.BuyerViewAuctionDetails;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * @author Potturi Sireesha
 * @version v 0.1 20/11/23 10:14 pm
 */
@SpringBootTest(classes = BiddingServiceApplication.class)
public class BuyerViewAuctionDetailsTest {
    @InjectMocks
    private BuyerViewAuctionDetails viewAuctionDetails;
    @Mock
    private AuctionRepository auctionRepository;
    @Mock
    private ProductRepository productRepository;

    @Mock
    BidSubscribeRepository bidSubscribeRepository;


    @Test
    public void testGetAuctionDetails() {

        String buyerId = "66795.BI";
        String auctionId = "45272.AI";

        Auction auction = new Auction();
        auction.setStartDate(LocalDateTime.now());
        auction.setEndDate(LocalDateTime.now());
        auction.setAuctionState(AuctionState.END_SUCCESS);
        auction.setAuctionProductState(AuctionProductState.SOLD);
        auction.setBasePrice(20.0);
        auction.setCurrentBid(100.0);
        auction.setAuctionId("45272.AI" );


        BidSubscribe bidSubscribe = new BidSubscribe();
        bidSubscribe.setAuctionId("45272.AI");

        when(bidSubscribeRepository.findByBidSubscribeId(auctionId+buyerId)).thenReturn(bidSubscribe);

        Product product = new Product();
        product.setDescription("MacBook Pro");
        product.setName("Apple");

        // Set up the behavior of the mocks
        when(auctionRepository.findByAuctionId(auctionId)).thenReturn(auction);
        when(productRepository.findByAuctionId(auctionId)).thenReturn(product);

        AuctionDetailResponse result = viewAuctionDetails.getAuctionDetails(buyerId, auctionId);

        // Verify the result
        assertNotNull(result);
        // Add more assertions based on your requirements and expected results
    }

    @Test
    public void testGetAuctionDetailsNullSellerId(){
        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> viewAuctionDetails.getAuctionDetails(null, null));

        assertEquals("BuyerId or AuctionId is null", exception.getMessage());

    }


    // Add more test cases as needed

}

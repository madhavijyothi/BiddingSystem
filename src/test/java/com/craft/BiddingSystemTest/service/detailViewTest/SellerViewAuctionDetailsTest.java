package com.craft.BiddingSystemTest.service.detailViewTest;

import com.craft.biddingSystem.BiddingServiceApplication;
import com.craft.biddingSystem.enums.AuctionProductState;
import com.craft.biddingSystem.enums.AuctionState;
import com.craft.biddingSystem.exception.AuthenticationException;
import com.craft.biddingSystem.exception.BadRequestException;
import com.craft.biddingSystem.models.Auction;
import com.craft.biddingSystem.models.Product;
import com.craft.biddingSystem.repository.AuctionRepository;
import com.craft.biddingSystem.repository.ProductRepository;
import com.craft.biddingSystem.response.AuctionDetailResponse;
import com.craft.biddingSystem.services.AuctionService;
import com.craft.biddingSystem.services.ProductService;
import com.craft.biddingSystem.services.detailView.SellerViewAuctionDetails;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * @author Potturi Sireesha
 * @version v 0.1 20/11/23 10:34 pm
 */
@SpringBootTest(classes = BiddingServiceApplication.class)
public class SellerViewAuctionDetailsTest {
    @InjectMocks
    private SellerViewAuctionDetails sellerViewAuctionDetails;
    @Mock
    private AuctionService auctionService;
    @Mock
    private ProductService productService;

    @Mock
    private AuctionRepository auctionRepository;

    @Mock
    private ProductRepository productRepository;


    @Test
    public void testGetAuctionDetails() {

        String sellerId = "32601.SI";
        String auctionId = "45272.AI";

        Auction auction = new Auction();
        auction.setStartDate(LocalDateTime.now());
        auction.setEndDate(LocalDateTime.now());
        auction.setAuctionState(AuctionState.END_SUCCESS);
        auction.setAuctionProductState(AuctionProductState.SOLD);
        auction.setBasePrice(20.0);
        auction.setCurrentBid(100.0);
        auction.setAuctionId("45272.AI" );
        auction.setSellerId("32601.SI");

        Product product = new Product();
        product.setDescription("MacBook Pro");
        product.setName("Apple");

        when(auctionService.getData(auctionId)).thenReturn(auction);
        when(auctionRepository.findByAuctionId(auctionId)).thenReturn(auction);
        when(productRepository.findByAuctionId(auctionId)).thenReturn(product);

        AuctionDetailResponse result = sellerViewAuctionDetails.getAuctionDetails(sellerId, auctionId);

        assertNotNull(result);

    }
    @Test
    public void testGetAuctionDetailsNullSellerId(){
        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> sellerViewAuctionDetails.getAuctionDetails(null, null));

        assertEquals("SellerId or AuctionId is null", exception.getMessage());

    }

    @Test
    public void testGetAuctionDetailsInValidSellerId(){
        Auction auction = new Auction();
        auction.setSellerId("32602.SI");
        when(auctionRepository.findByAuctionId(any())).thenReturn(auction);
        AuthenticationException exception = assertThrows(AuthenticationException.class,
                () -> sellerViewAuctionDetails.getAuctionDetails("32601.SI", "32601.PI"));

        assertEquals("This auction does not belong to the seller who requested auction data.", exception.getMessage());

    }
}

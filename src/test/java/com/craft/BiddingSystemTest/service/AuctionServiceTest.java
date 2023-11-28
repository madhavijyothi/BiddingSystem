package com.craft.BiddingSystemTest.service;

import com.craft.biddingSystem.enums.AuctionProductState;
import com.craft.biddingSystem.enums.AuctionState;
import com.craft.biddingSystem.enums.ObserverType;
import com.craft.biddingSystem.exception.BadRequestException;
import com.craft.biddingSystem.models.Auction;
import com.craft.biddingSystem.models.BidSubscribe;
import com.craft.biddingSystem.models.Product;
import com.craft.biddingSystem.repository.AuctionRepository;
import com.craft.biddingSystem.repository.BidSubscribeRepository;
import com.craft.biddingSystem.repository.ProductRepository;
import com.craft.biddingSystem.request.AuctionRequest;
import com.craft.biddingSystem.services.AuctionService;
import com.craft.biddingSystem.services.BuyerService;
import com.craft.biddingSystem.services.SellerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class AuctionServiceTest {

    @InjectMocks
    private AuctionService auctionService;

    @Mock
    private AuctionRepository auctionRepository;

    @Mock
    private BuyerService buyerService;

    @Mock
    private SellerService sellerService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private BidSubscribeRepository bidSubscribeRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateAuction() {
        String sellerId = "7001.SI";
        AuctionRequest auctionRequest = new AuctionRequest();
        auctionRequest.setSellerId(sellerId);
        auctionRequest.setAuctionState(AuctionState.DRAFTED);
        when(sellerService.validateSeller(sellerId)).thenReturn(true);
        when(productRepository.findByAuctionId(any())).thenReturn(null);
        when(auctionRepository.save(any())).thenReturn(new Auction());
        when(productRepository.save(any())).thenReturn(new Product());
        auctionService.createAuction(auctionRequest);
        verify(auctionRepository, times(1)).save(any());
    }


    @Test
    public void testUpdateAuctionDetails() {
        AuctionRequest auctionRequest = new AuctionRequest();
        auctionRequest.setAuctionId("45272.AI");
        auctionRequest.setSellerId("seller1");
        auctionRequest.setBasePrice(100.0);
        auctionRequest.setProductName("product1");
        auctionRequest.setProductDescription("description1");
        auctionRequest.setAuctionState(AuctionState.DRAFTED);

        Auction auction = new Auction();
        auction.setAuctionId("45272.AI");
        auction.setSellerId("seller1");
        auction.setBasePrice(100.0);
        auction.setAuctionState(AuctionState.DRAFTED);

        when(sellerService.validateSeller(auctionRequest.getSellerId())).thenReturn(true);
        when(auctionRepository.findByAuctionId(auctionRequest.getAuctionId())).thenReturn(auction);
        when(auctionRepository.save(any(Auction.class))).thenReturn(auction);
        when(productRepository.save(any())).thenReturn(new Product());
        when(productRepository.findByAuctionId(any())).thenReturn(new Product());

        auctionService.updateAuctionDetails(auctionRequest);

        verify(auctionRepository, times(1)).save(any(Auction.class));
        verify(productRepository, times(1)).save(any());
    }

    @Test
    public void testUpdateAuctionDetailsInvalidSellerId() {
        AuctionRequest auctionRequest = new AuctionRequest();
        auctionRequest.setAuctionId("45272.AI");
        auctionRequest.setSellerId("seller1");
        auctionRequest.setBasePrice(100.0);
        auctionRequest.setProductName("product1");
        auctionRequest.setProductDescription("description1");
        auctionRequest.setAuctionState(AuctionState.DRAFTED);

        Auction auction = new Auction();
        auction.setAuctionId("45272.AI");
        auction.setSellerId("seller1");
        auction.setBasePrice(100.0);
        auction.setAuctionState(AuctionState.DRAFTED);

        when(sellerService.validateSeller(auctionRequest.getSellerId())).thenReturn(false);
        when(auctionRepository.findByAuctionId(auctionRequest.getAuctionId())).thenReturn(auction);
        when(auctionRepository.save(any(Auction.class))).thenReturn(auction);
        when(productRepository.save(any())).thenReturn(new Product());
        when(productRepository.findByAuctionId(any())).thenReturn(new Product());

        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> auctionService.updateAuctionDetails(auctionRequest));

    }

    @Test
    public void testUpdateAuctionDetailsNullAuctionId() {
        AuctionRequest auctionRequest = new AuctionRequest();
        auctionRequest.setSellerId("seller1");
        auctionRequest.setBasePrice(100.0);
        auctionRequest.setProductName("product1");
        auctionRequest.setProductDescription("description1");
        auctionRequest.setAuctionState(AuctionState.DRAFTED);

        Auction auction = new Auction();
        auction.setAuctionId("45272.AI");
        auction.setSellerId("seller1");
        auction.setBasePrice(100.0);
        auction.setAuctionState(AuctionState.DRAFTED);

        when(sellerService.validateSeller(auctionRequest.getSellerId())).thenReturn(true);
        when(auctionRepository.findByAuctionId(auctionRequest.getAuctionId())).thenReturn(auction);
        when(auctionRepository.save(any(Auction.class))).thenReturn(auction);
        when(productRepository.save(any())).thenReturn(new Product());
        when(productRepository.findByAuctionId(any())).thenReturn(new Product());

        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> auctionService.updateAuctionDetails(auctionRequest));

    }

    @Test
    public void testUpdateAuctionDetailsNullAuction() {
        AuctionRequest auctionRequest = new AuctionRequest();
        auctionRequest.setAuctionId("45272.AI");
        auctionRequest.setSellerId("seller1");
        auctionRequest.setBasePrice(100.0);
        auctionRequest.setProductName("product1");
        auctionRequest.setProductDescription("description1");
        auctionRequest.setAuctionState(AuctionState.DRAFTED);

        Auction auction = new Auction();
        auction.setAuctionId("45272.AI");
        auction.setSellerId("seller1");
        auction.setBasePrice(100.0);
        auction.setAuctionState(AuctionState.DRAFTED);

        when(sellerService.validateSeller(auctionRequest.getSellerId())).thenReturn(true);
        when(auctionRepository.findByAuctionId(auctionRequest.getAuctionId())).thenReturn(null);
        when(auctionRepository.save(any(Auction.class))).thenReturn(auction);
        when(productRepository.save(any())).thenReturn(new Product());
        when(productRepository.findByAuctionId(any())).thenReturn(new Product());

        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> auctionService.updateAuctionDetails(auctionRequest));
    }


    @Test
    public void testSubscribeToAuction() {
        String buyerId = "66795.BI";
        String auctionId = "45272.AI";
        double bidPrice = 100.0;

        when(buyerService.validateBuyer(buyerId)).thenReturn(true);
        when(bidSubscribeRepository.findByBidSubscribeId(auctionId + buyerId)).thenReturn(null);

        auctionService.subscribeToAuction(buyerId, auctionId, bidPrice);

        verify(bidSubscribeRepository, times(1)).save(any(BidSubscribe.class));
    }


    @Test
    public void testPlaceBid() {
        String buyerId = "66795.BI";
        String auctionId = "45272.AI";
        String productId = "product1";
        double bidPrice = 100.0;

        Auction auction = new Auction();
        auction.setAuctionState(AuctionState.STARTED);
        auction.setAuctionProductState(AuctionProductState.UNBID);
        auction.setCurrentBid(10.0);
        auction.setAuctionId(auctionId);

        when(auctionRepository.findByAuctionId(auctionId)).thenReturn(auction);

        BidSubscribe bidSubscribe = new BidSubscribe();
        bidSubscribe.setAuctionId(auctionId);

        when(buyerService.validateBuyer(buyerId)).thenReturn(true);
        when(auctionRepository.findByAuctionId(auctionId)).thenReturn(auction);
        when(auctionRepository.save(auction)).thenReturn(auction);
        when(bidSubscribeRepository.findByBidSubscribeId(auctionId + buyerId)).thenReturn(bidSubscribe);
        when(bidSubscribeRepository.save(bidSubscribe)).thenReturn(bidSubscribe);
        when(bidSubscribeRepository.findByAuctionId(auction.getAuctionId())).thenReturn(Arrays.asList(bidSubscribe));

        Product product = new Product();
        product.setProductId(productId);
        when(productRepository.findByAuctionId(auctionId)).thenReturn(product);


        auctionService.placeBid(auctionId, productId, buyerId, bidPrice);


    }

    @Test
    public void testStartAuction() {
        String auctionId = "45272.AI";
        Auction auction = new Auction();
        auction.setAuctionId(auctionId);

        when(auctionRepository.findByAuctionId(auctionId)).thenReturn(auction);

        auctionService.startAuction(auction, auctionId);

        verify(auctionRepository, times(1)).save(any(Auction.class));
    }

    @Test
    public void testEndAuctionUnBid() {
        String auctionId = "45272.AI";
        Auction auction = new Auction();
        auction.setAuctionId(auctionId);
        auction.setAuctionProductState(AuctionProductState.UNBID);

        when(auctionRepository.findByAuctionId(auctionId)).thenReturn(auction);
        when(auctionRepository.save(auction)).thenReturn(auction);

        BidSubscribe bidSubscribe = new BidSubscribe();
        bidSubscribe.setAuctionId(auctionId);

        Product product = new Product();
        product.setProductId("71234.PI");
        when(productRepository.findByAuctionId(auctionId)).thenReturn(product);
        when(auctionRepository.save(auction)).thenReturn(auction);
        when(bidSubscribeRepository.findByAuctionId(auction.getAuctionId())).thenReturn(Arrays.asList(bidSubscribe));
        auctionService.endAuction(auction);
        verify(auctionRepository, times(1)).save(any(Auction.class));
        assertTrue(auction.getAuctionProductState() == AuctionProductState.FAILED);
        assertTrue(auction.getAuctionState() == AuctionState.END_FAILED);
    }


    @Test
    public void testEndAuctionSold() {
        String auctionId = "45272.AI";
        Auction auction = new Auction();
        auction.setAuctionId(auctionId);
        auction.setAuctionProductState(AuctionProductState.BID);
        auction.setAuctionState(AuctionState.STARTED);
        auction.setCurrentBid(100.0);
        auction.setBasePrice(20.0);

        when(auctionRepository.findByAuctionId(auctionId)).thenReturn(auction);
        when(auctionRepository.save(auction)).thenReturn(auction);

        BidSubscribe bidSubscribe = new BidSubscribe();
        bidSubscribe.setAuctionId(auctionId);

        Product product = new Product();
        product.setProductId("71234.PI");
        when(productRepository.findByAuctionId(auctionId)).thenReturn(product);
        when(auctionRepository.save(auction)).thenReturn(auction);
        when(bidSubscribeRepository.findByAuctionId(auction.getAuctionId())).thenReturn(Arrays.asList(bidSubscribe));
        auctionService.endAuction(auction);
        verify(auctionRepository, times(1)).save(any(Auction.class));
        assertTrue(auction.getAuctionProductState() == AuctionProductState.SOLD);

    }


    @Test
    public void testValidateAuction() {
        String auctionId = "45272.AI";

        when(auctionRepository.findByAuctionId(auctionId)).thenReturn(new Auction());

        boolean result = auctionService.validateAuction(auctionId);

        verify(auctionRepository, times(1)).findByAuctionId(anyString());
        assertTrue(result);
    }


    @Test
    public void testGetData() {
        String auctionId = "45272.AI";
        Auction auction = new Auction();
        auction.setAuctionId(auctionId);

        when(auctionRepository.findByAuctionId(auctionId)).thenReturn(auction);

        Auction result = auctionService.getData(auctionId);

        verify(auctionRepository, times(1)).findByAuctionId(anyString());
    }

    @Test
    public void testUnsubscribe(){
        Auction auction = new Auction();
        auction.setAuctionId("700001.AI");
        auction.setCurrentWinningBuyerId("70002.BI");

       when(auctionRepository.findByAuctionId(anyString())).thenReturn(auction);
       when(bidSubscribeRepository.findByBidSubscribeId(anyString())).thenReturn(new BidSubscribe());
         auctionService.unsubscribe("700001.AI", "700001.SI", ObserverType.BUYER);
        verify(auctionRepository, times(2)).findByAuctionId(anyString());
        verify(bidSubscribeRepository, times(1)).findByBidSubscribeId(anyString());

    }

    @Test
    public void testUnsubscribeNotEligilble(){
        Auction auction = new Auction();
        auction.setAuctionId("700001.AI");
        auction.setCurrentWinningBuyerId("700001.AI");

        when(auctionRepository.findByAuctionId(anyString())).thenReturn(auction);
        when(bidSubscribeRepository.findByBidSubscribeId(anyString())).thenReturn(new BidSubscribe());
        auctionService.unsubscribe("700001.AI", "700001.SI", ObserverType.BUYER);
        verify(auctionRepository, times(2)).findByAuctionId(anyString());
        verify(bidSubscribeRepository, times(1)).findByBidSubscribeId(anyString());

    }


}
package com.craft.BiddingSystemTest.service;

import com.craft.biddingSystem.BiddingServiceApplication;
import com.craft.biddingSystem.exception.AuthenticationException;
import com.craft.biddingSystem.models.Event;
import com.craft.biddingSystem.models.Seller;
import com.craft.biddingSystem.repository.AuctionRepository;
import com.craft.biddingSystem.repository.ProductRepository;
import com.craft.biddingSystem.repository.SellerRepository;
import com.craft.biddingSystem.request.SellerRequest;
import com.craft.biddingSystem.services.SellerService;
import com.craft.biddingSystem.utils.Utils;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@SpringBootTest(classes = BiddingServiceApplication.class)
public class SellerServiceTest {

    @Mock
    private SellerRepository sellerRepository;

    @Mock
    private AuctionRepository auctionRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private Utils utils;  // Assuming Utils is a utility class with static methods

    @InjectMocks
    private SellerService sellerService;


    @Test
    void testRegister_ValidInput() {
        SellerRequest sellerRequest = new SellerRequest();
        sellerRequest.setUsername("testUser");
        sellerRequest.setEmailId("test@gmail.com");
        sellerRequest.setPhoneNo("1234567890");
        sellerRequest.setPassword("xyzzzz");


        Seller seller = new Seller();
        seller.setSellerId("");
        seller.setSellerName("testUser");
        seller.setSellerEmailId("test@gmail.com");
        seller.setSellerPhoneNo("1234567890");
        seller.setSellerPassword("xyzzzz");

        when(sellerRepository.save(seller)).thenReturn(seller);
        when(sellerRepository.findBySellerPhoneNo("1234567890")).thenReturn(null);

        sellerService.register(sellerRequest);

    }


    @Test
    void testAuthenticate_SuccessfulAuthentication() {
        Seller mockSeller = new Seller();
        mockSeller.setSellerPhoneNo("1234567890");
        mockSeller.setSellerPassword("password123");
        when(sellerRepository.findBySellerPhoneNo("1234567890")).thenReturn(mockSeller);
        sellerService.authenticate("1234567890", "password123");


    }

    @Test
    void testAuthenticate_FailedAuthentication() {
        when(sellerRepository.findBySellerPhoneNo("invalidPhoneNo")).thenReturn(null);
        AuthenticationException exception = assertThrows(AuthenticationException.class, () ->
                sellerService.authenticate("0","invalidPassword"));

        assertEquals("Invalid credentials", exception.getMessage());
    }

//    @Test
//    void testViewAllSellerOwnedBids_ValidSellerId() {
//        // Mock the behavior of the auctionRepository
//        Auction auction1 = new Auction();
//        auction1.setAuctionId("70001.AI");
//        auction1.setBasePrice(20.0);
//        auction1.setCurrentBid(100.0);
//        Auction auction2 = new Auction();
//        auction2.setAuctionId("70002.AI");
//        auction2.setBasePrice(10.0);
//        auction2.setCurrentBid(50.0);
//
//        when(auctionRepository.findBySellerId(any()))
//                .thenReturn(Arrays.asList(
//                        auction1,auction2
//                ));
//
//        // Mock the behavior of the productRepository
//        Product product1 = new Product();
//        product1.setProductId("1000.PI");
//        Product product2 = new Product();
//        product2.setProductId("5000.PI");
//        when(productRepository.findByAuctionId("70001.AI"))
//                .thenReturn(product1);
//        when(productRepository.findByAuctionId("70002.AI"))
//                .thenReturn(product2);
//
//        Seller seller = new Seller();
//        seller.setSellerId("32601.SI");
//
//        when(sellerRepository.findBySellerId(any())).thenReturn(seller);
//
//        when(auctionRepository.findBySellerIdAndAuctionState("32601.SI", AuctionState.STARTED)).thenReturn(Arrays.asList(
//                auction1,auction2
//        ));
//
//
//        List<Auction> auctions = auctionRepository.findBySellerIdAndAuctionState("32601.SI", AuctionState.STARTED);
//
//        // Perform the viewAllSellerOwnedBids method with a valid sellerId
//        BidListQueryResponse bidListQueryResponse = sellerService.viewAllSellerOwnedBids("32601.SI");
//         assertNotNull(bidListQueryResponse);
//
//
//    }

//    @Test
//    void testViewAllSellerOwnedBids_InvalidSellerId() {
//        // Mock the behavior of the auctionRepository for an invalid sellerId
//        when(auctionRepository.findBySellerId("invalidSellerId")).thenReturn(null);
//        BadRequestException exception = assertThrows(BadRequestException.class, () ->
//                sellerService.viewAllSellerOwnedBids("invalidSellerId"));
//
//        assertEquals("Invalid SellerId", exception.getMessage());
//    }


    @Test
    void testUpdateObserver(){
        List<String> sellerIds = new ArrayList<>();
        sellerIds.add("32601.SI");
        Event event = new Event();
        event.setMessage("Auction is started");
        Seller seller = new Seller();
        seller.setSellerId("32601.SI");
        when(sellerRepository.findBySellerId(any())).thenReturn(seller);
        sellerService.updateObserver(sellerIds, event);
    }



}

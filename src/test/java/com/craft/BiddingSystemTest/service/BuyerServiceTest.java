package com.craft.BiddingSystemTest.service;

import com.craft.biddingSystem.BiddingServiceApplication;
import com.craft.biddingSystem.exception.AuthenticationException;
import com.craft.biddingSystem.exception.BadRequestException;
import com.craft.biddingSystem.models.BidSubscribe;
import com.craft.biddingSystem.models.Buyer;
import com.craft.biddingSystem.models.Event;
import com.craft.biddingSystem.repository.BidSubscribeRepository;
import com.craft.biddingSystem.repository.BuyerRepository;
import com.craft.biddingSystem.request.BuyerRequest;
import com.craft.biddingSystem.response.BidDetails;
import com.craft.biddingSystem.response.BidListQueryResponse;
import com.craft.biddingSystem.services.BuyerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@SpringBootTest(classes = BiddingServiceApplication.class)
public class BuyerServiceTest {


    @Autowired
    BuyerService buyerService;

    @MockBean
    BuyerRepository buyerRepository;

    @MockBean
    BidSubscribeRepository bidSubscribeRepository;



    @Test
    public void testRegister_ValidInput() {
        // Arrange
        BuyerRequest buyerRequest = new BuyerRequest();
        buyerRequest.setPhoneNo("1234567890");
        buyerRequest.setEmailId("xyz@gmail.com");
        buyerRequest.setUsername("JohnDoe");
        buyerRequest.setPassword("securepassword");


        Buyer buyer = new Buyer();
        buyer.setBuyerId("1234567");
        buyer.setBuyerName("JohnDoe");
        buyer.setBuyerEmailId("xyz@gmail.com");
        buyer.setBuyerPhoneNo("1234567890");
        buyer.setBuyerPassword("securepassword");

        when(buyerRepository.save(buyer)).thenReturn(buyer);
        buyerService.register(buyerRequest);

    }




    @Test
    void testAuthenticate_SuccessfulAuthentication() {
        Buyer mockBuyer = new Buyer();
        mockBuyer.setBuyerPhoneNo("1234567890");
        mockBuyer.setBuyerPassword("password123");
        when(buyerRepository.findByBuyerPhoneNo("1234567890")).thenReturn(mockBuyer);

        // Perform the authentication
        Buyer authenticatedBuyer = buyerService.authenticate("1234567890", "password123");

        // Verify that the authentication was successful
        assertNotNull(authenticatedBuyer);
        assertEquals("1234567890", authenticatedBuyer.getBuyerPhoneNo());
    }

    @Test
    void testAuthenticate_FailedAuthentication() {
        // Mock the behavior of the buyerRepository for a non-existent buyer
        when(buyerRepository.findByBuyerPhoneNo("invalidPhoneNo")).thenReturn(null);
        AuthenticationException exception = assertThrows(AuthenticationException.class, () ->
                buyerService.authenticate("0","invalidPassword"));

        assertEquals("Invalid credentials", exception.getMessage());
    }

    @Test
    void testViewAllBuyerBids_ValidBuyerId() {
        Buyer buyer = new Buyer();
        buyer.setBuyerId("1234567");
        buyer.setBuyerName("JohnDoe");
        buyer.setBuyerEmailId("xyz@gmail.com");
        buyer.setBuyerPhoneNo("1234567890");
        buyer.setBuyerPassword("securepassword");

        when(buyerRepository.findByBuyerId("70001.BI"))
                .thenReturn(buyer);
        when(bidSubscribeRepository.findByBuyerId("70001.BI"))
                .thenReturn(Arrays.asList(
                        new BidSubscribe("1234567","12345.AI", "70001.BI", 100.00,new Date()),
                        new BidSubscribe("1234577","12346.AI", "70001.BI", 110.00, new  Date() )
                ));
        BidListQueryResponse bidListQueryResponse = new BidListQueryResponse();
        BidDetails bidDetails = new BidDetails();
        bidDetails.setBidAmount(100.00);
        bidDetails.setAuctionId("12345.AI");
        bidDetails.setBasePrice(20.0);
        bidListQueryResponse.setBuyerBidDetailsList(Arrays.asList(bidDetails));

        bidListQueryResponse = buyerService.viewAllBuyerBids("70001.BI");

        // Verify the results
        assertNotNull(bidListQueryResponse);

    }

    @Test
    void testViewAllBuyerBids_InvalidBuyerId() {



        when(bidSubscribeRepository.findByBuyerId("70002.BI")).thenReturn(null);

        BidListQueryResponse bidListQueryResponse = new BidListQueryResponse();
        BadRequestException exception = assertThrows(BadRequestException.class, () ->
                buyerService.viewAllBuyerBids("invalidSellerId"));
        assertEquals("Invalid buyerId", exception.getMessage());

    }

    @Test
    void testUpdateObserver(){
        List<String> buyers = new ArrayList<>();
        buyers.add("32601.SI");
        Event event = new Event();
        event.setMessage("Auction is started");
        Buyer buyer = new Buyer();
        buyer.setBuyerId("32601.SI");
        when(buyerRepository.findByBuyerId(any())).thenReturn(buyer);
        buyerService.updateObserver(buyers, event);
    }



}

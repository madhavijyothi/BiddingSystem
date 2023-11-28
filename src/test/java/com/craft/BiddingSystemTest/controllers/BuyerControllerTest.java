package com.craft.BiddingSystemTest.controllers;


import com.craft.biddingSystem.controllers.BuyerController;
import com.craft.biddingSystem.enums.AuctionProductState;
import com.craft.biddingSystem.enums.AuctionState;
import com.craft.biddingSystem.exception.AuthenticationException;
import com.craft.biddingSystem.exception.BadRequestException;
import com.craft.biddingSystem.models.BidSubscribe;
import com.craft.biddingSystem.models.Buyer;
import com.craft.biddingSystem.repository.BuyerRepository;
import com.craft.biddingSystem.request.BuyerRequest;
import com.craft.biddingSystem.response.AuctionDetailResponse;
import com.craft.biddingSystem.services.AuctionService;
import com.craft.biddingSystem.services.BuyerService;
import com.craft.biddingSystem.services.detailView.BuyerViewAuctionDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class BuyerControllerTest {


    @Mock
    private BuyerRepository buyerRepository;

    @Mock
    private BuyerService buyerService;

    @Mock
    private AuctionService auctionService;

    @Mock
    private BuyerViewAuctionDetails buyerViewAuctionDetails;

    @InjectMocks
    private BuyerController buyerController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(buyerController).build();
    }

    @Test
    void testRegisterUser() throws Exception {

        BuyerRequest buyerRequest = new BuyerRequest();
        buyerRequest.setUsername("John Doe");
        buyerRequest.setEmailId("john.doe@example.com");
        buyerRequest.setPhoneNo("1234567890");
        buyerRequest.setPassword("password123");

        String jsonRequest = objectMapper.writeValueAsString(buyerRequest);


        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/buyer/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest);


        ResultActions resultActions = mockMvc.perform(requestBuilder);
        resultActions.andExpect(status().isCreated());

    }


    @Test
    void testLoginBuyer_SuccessfulAuthentication() throws Exception {

        Map<String, String> credentials = new HashMap<>();
        credentials.put("phoneNo", "1234567890");
        credentials.put("password", "password123");
        Buyer buyer = new Buyer();
        buyer.setBuyerPassword("password123");

       when(buyerService.authenticate(any(String.class), any(String.class))).thenReturn(new Buyer());


        String jsonRequest = objectMapper.writeValueAsString(credentials);

        // Create a POST request
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/buyer/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest);

        // Perform the request and assert the status
        ResultActions resultActions = mockMvc.perform(requestBuilder);
        resultActions.andExpect(status().isOk());
        // Add more assertions based on your specific use case
    }

    @Test
    void testLoginBuyer_FailedAuthentication() throws Exception {

        Map<String, String> credentials = new HashMap<>();
        credentials.put("phoneNo", "1234567890");
        credentials.put("password", "wrongPassword");

        String jsonRequest = objectMapper.writeValueAsString(credentials);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/buyer/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest);

        ResultActions resultActions = mockMvc.perform(requestBuilder);
    }

    @Test
    public void testRegisterBuyerSuccess() {
        BuyerRequest buyerRequest = new BuyerRequest();
        ResponseEntity<String> responseEntity = buyerController.registerBuyer(buyerRequest);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals("Registration is successful", responseEntity.getBody());
        verify(buyerService, times(1)).register(buyerRequest);
    }

    @Test
    public void testRegisterBuyerException() {
        // Arrange
        BuyerRequest buyerRequest = new BuyerRequest();
        doThrow(new BadRequestException("SomeExceptionMessage")).when(buyerService).register(buyerRequest);

        // Act
        ResponseEntity<String> responseEntity = buyerController.registerBuyer(buyerRequest);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Error in registration : SomeExceptionMessage", responseEntity.getBody());
        verify(buyerService, times(1)).register(buyerRequest);
    }


    @Test
    public void testLoginBuyerSuccess() {
        // Arrange
        Map<String, String> credentials = new HashMap<>();
        credentials.put("phoneNo", "1234567890");
        credentials.put("password", "securePassword");

        // Act
        ResponseEntity<String> responseEntity = buyerController.loginBuyer(credentials);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Authentication successful", responseEntity.getBody());
        verify(buyerService, times(1)).authenticate(credentials.get("phoneNo"), credentials.get("password"));
    }

    @Test
    public void testLoginBuyerFailedAuthentication() {
        // Arrange
        Map<String, String> credentials = new HashMap<>();
        credentials.put("phoneNo", "1234567890");
        credentials.put("password", "incorrectPassword");
        when(buyerService.authenticate(credentials.get("phoneNo"), credentials.get("password")))
                .thenThrow(new AuthenticationException("Invalid credentials"));

        // Act
        ResponseEntity<String> responseEntity = buyerController.loginBuyer(credentials);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Authentication failed :Invalid credentials", responseEntity.getBody());
        verify(buyerService, times(1)).authenticate(credentials.get("phoneNo"), credentials.get("password"));
    }

    @Test
    public void testGetAllBuyerBidsException() {
        List<BidSubscribe> bidSubscribes = new ArrayList<>();
       when(buyerService.getBidSubscribesByBuyer(any(String.class))).thenReturn(bidSubscribes) ;

        ResponseEntity<List<BidSubscribe>> responseEntity = buyerController.getBidSubscribesByBuyer("123456.BI");

        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        assertEquals(null, responseEntity.getBody());
        verify(buyerService, times(1)).getBidSubscribesByBuyer(any(String.class));

    }

    @Test
    public void testGetAuctionDetailsSuccess() {
        // Arrange
        Map<String, String> credentials = new HashMap<>();
        credentials.put("buyerId", "123567.BI");
        credentials.put("auctionId", "123567.AI");

        AuctionDetailResponse expectedResponse = new AuctionDetailResponse();
        expectedResponse.setAuctionId("123567.AI");
        expectedResponse.setCategory("Laptop");
        expectedResponse.setAuctionState(AuctionState.STARTED);
        expectedResponse.setAuctionProductState(AuctionProductState.BID);
        expectedResponse.setBasePrice(100.0);
        expectedResponse.setCurrentBid(200.0);
        expectedResponse.setProductId("456789");
        expectedResponse.setProductName("Dell");
        expectedResponse.setProductDescription("Dell Laptop");
        when(auctionService.getAuctionDetails(credentials.get("buyerId"), credentials.get("auctionId"), "BuyerView"))
                .thenReturn(expectedResponse);

        // Act
        AuctionDetailResponse auctionDetailResponse = buyerController.getAuctionDetails(credentials);

        verify(auctionService, times(1)).getAuctionDetails(credentials.get("buyerId"), credentials.get("auctionId"), "BuyerView");
    }

    @Test
    public void testGetAuctionDetailsException() {
        // Arrange
        Map<String, String> credentials = new HashMap<>();
        credentials.put("buyerId", "123");
        credentials.put("auctionId", "456");

        when(auctionService.getAuctionDetails(credentials.get("buyerId"), credentials.get("auctionId"), "BuyerView"))
                .thenThrow(new RuntimeException("No such auction exists for the buyer"));
        AuctionDetailResponse auctionDetailResponse = new AuctionDetailResponse();

        auctionDetailResponse = buyerController.getAuctionDetails(credentials);

        verify(auctionService, times(1)).getAuctionDetails(credentials.get("buyerId"), credentials.get("auctionId"), "BuyerView");
    }










}

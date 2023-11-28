package com.craft.BiddingSystemTest.controllers;

import com.craft.biddingSystem.controllers.AuctionController;
import com.craft.biddingSystem.controllers.SellerController;
import com.craft.biddingSystem.exception.BadRequestException;
import com.craft.biddingSystem.repository.BuyerRepository;
import com.craft.biddingSystem.request.SellerRequest;
import com.craft.biddingSystem.response.AuctionDetailResponse;
import com.craft.biddingSystem.services.AuctionService;
import com.craft.biddingSystem.services.SellerService;
import com.craft.biddingSystem.services.detailView.SellerViewAuctionDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


/**
 * @author Potturi Sireesha
 * @version v 0.1 20/11/23 4:25 pm
 */
public class SellerControllerTest {

    @Mock
    private BuyerRepository buyerRepository;

    @Mock
    private SellerService sellerService;

    @Mock
    private SellerViewAuctionDetails sellerViewAuctionDetails;

    @Mock
    private AuctionService auctionService;

    @Mock
    private AuctionController auctionController;

    @InjectMocks
    private SellerController sellerController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize MockMvc
        mockMvc = MockMvcBuilders.standaloneSetup(sellerController).build();
    }


    @Test
    public void testRegisterSellerSuccess() {
        SellerRequest sellerRequest = new SellerRequest(/* initialize with test data */);
        ResponseEntity<String> responseEntity = sellerController.registerSeller(sellerRequest);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals("Registration is successful", responseEntity.getBody());
        verify(sellerService, times(1)).register(sellerRequest);
    }

    @Test
    public void testRegisterSellerException() {
        SellerRequest sellerRequest = new SellerRequest();
        doThrow(new BadRequestException("SomeExceptionMessage")).when(sellerService).register(sellerRequest);
        ResponseEntity<String> responseEntity = sellerController.registerSeller(sellerRequest);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Error in registration : SomeExceptionMessage", responseEntity.getBody());
        verify(sellerService, times(1)).register(sellerRequest);
    }

    @Test
    public void testLoginSellerSuccess() {
        Map<String, String> credentials = new HashMap<>();
        credentials.put("phoneNo", "1234567890");
        credentials.put("password", "securePassword");

        ResponseEntity<String> responseEntity = sellerController.loginSeller(credentials);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Authentication successful", responseEntity.getBody());
        verify(sellerService, times(1)).authenticate(credentials.get("phoneNo"), credentials.get("password"));
    }

    @Test
    public void testLoginSellerFailedAuthentication() {

        Map<String, String> credentials = new HashMap<>();
        credentials.put("phoneNo", "1234567890");
        credentials.put("password", "incorrectPassword");
        doThrow(new BadRequestException("Invalid credentials")).when(sellerService).authenticate(credentials.get("phoneNo"), credentials.get("password"));
        ResponseEntity<String> responseEntity = sellerController.loginSeller(credentials);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Authentication failed Invalid credentials", responseEntity.getBody());
        verify(sellerService, times(1)).authenticate(credentials.get("phoneNo"), credentials.get("password"));
    }




    @Test
    public void testGetAuctionDetailsSuccess() {
        Map<String, String> credentials = new HashMap<>();
        credentials.put("sellerId", "123");
        credentials.put("auctionId", "456");

        AuctionDetailResponse expectedResponse = new AuctionDetailResponse();
        when(auctionService.getAuctionDetails(credentials.get("sellerId"), credentials.get("auctionId"), "SellerView"))
                .thenReturn(expectedResponse);
        sellerController.getAuctionDetails(credentials);

        verify(auctionService, times(1)).getAuctionDetails(credentials.get("sellerId"), credentials.get("auctionId"), "SellerView");
    }

    @Test
    public void testGetAuctionDetailsException() {
        // Arrange
        Map<String, String> credentials = new HashMap<>();
        credentials.put("sellerId", "123");
        credentials.put("auctionId", "456");
        when(auctionService.getAuctionDetails(credentials.get("sellerId"), credentials.get("auctionId"), "SellerView"))
                .thenThrow(new RuntimeException());

        sellerController.getAuctionDetails(credentials);
        verify(auctionService, times(1)).getAuctionDetails(credentials.get("sellerId"), credentials.get("auctionId"), "SellerView");
    }




}

package com.craft.BiddingSystemTest.controllers;

import com.craft.biddingSystem.controllers.AuctionController;
import com.craft.biddingSystem.services.AuctionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuctionControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AuctionService auctionService;

    @InjectMocks
    private AuctionController auctionController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
      mockMvc = MockMvcBuilders.standaloneSetup(auctionController).build();
    }



    @Test
    public void testPlaceBid() throws Exception {
        String auctionId = "45272.AI";
        String productId = "326012.PI";
        String buyerId = "32601.BI";
        double bidAmount = 100.0;


        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/auction/placeBid")
                        .param("auctionId", auctionId)
                        .param("productId", productId)
                        .param("buyerId", buyerId)
                        .param("bidAmount", String.valueOf(bidAmount)))
                .andExpect(status().isOk())
                .andExpect(content().string("Bid placed successfully."))
                .andReturn();

        verify(auctionService, times(1)).placeBid(auctionId, productId, buyerId, bidAmount);
    }

    @Test
    public void testPlaceBidException() throws Exception {
        String auctionId = "45272.AI";
        String productId = "326012.PI";
        String buyerId = "32601.BI";
        double bidAmount = 100.0;


        doThrow(new RuntimeException("Buyer Id is invalid")).when(auctionService).placeBid(any(String.class),any(String.class),any(String.class),any(double.class));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/auction/placeBid")
                        .param("auctionId", auctionId)
                        .param("productId", productId)
                        .param("buyerId", buyerId)
                        .param("bidAmount", String.valueOf(bidAmount)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Error placing bid: Buyer Id is invalid"))
                .andReturn();

        verify(auctionService, times(1)).placeBid(auctionId, productId, buyerId, bidAmount);
    }


    @Test
    public void testUnsubscribe() throws Exception {
        Map<String, String> credentials = new HashMap<>();
        credentials.put("auctionId", "yourAuctionId");
        credentials.put("buyerId", "yourBuyerId");

        mockMvc.perform(MockMvcRequestBuilders.post("/auction/unsubscribe")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(credentials)))
                .andExpect(status().isOk())
                .andExpect(content().string("Unsubscribed successfully."));
    }


    }



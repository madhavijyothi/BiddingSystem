package com.craft.biddingSystem.controllers;

import com.craft.biddingSystem.models.BidSubscribe;
import com.craft.biddingSystem.request.BuyerRequest;
import com.craft.biddingSystem.response.AuctionDetailResponse;
import com.craft.biddingSystem.services.AuctionService;
import com.craft.biddingSystem.services.BuyerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/buyer")
public class BuyerController {
    @Autowired
    private BuyerService buyerService;

    @Autowired
    private AuctionService auctionService;


    @PostMapping("/register")
    public ResponseEntity<String> registerBuyer(@RequestBody @Valid BuyerRequest buyerRequest) {
        try {
            buyerService.register(buyerRequest);
            return new ResponseEntity<>("Registration is successful", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error in registration : " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginBuyer(@RequestBody Map<String, String> credentials) {
        try {
            buyerService.authenticate(credentials.get("phoneNo"), credentials.get("password"));
            return new ResponseEntity<>("Authentication successful", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Authentication failed :" + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/allBidDetails/{buyerId}")
    public ResponseEntity<List<BidSubscribe>> getBidSubscribesByBuyer(@PathVariable String buyerId) {
        List<BidSubscribe> bidSubscribes = buyerService.getBidSubscribesByBuyer(buyerId);

        if (bidSubscribes.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(bidSubscribes);
        }
    }

    @PostMapping("/auction/details")
    public AuctionDetailResponse getAuctionDetails(@RequestBody Map<String, String> credentials) {
        try {
            AuctionDetailResponse response = auctionService.getAuctionDetails(credentials.get("buyerId"), credentials.get("auctionId"), "BuyerView");
            return response;
        } catch (Exception e) {
            return null;

        }
    }

}


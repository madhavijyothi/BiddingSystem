package com.craft.biddingSystem.controllers;

import com.craft.biddingSystem.enums.ObserverType;
import com.craft.biddingSystem.request.AuctionRequest;
import com.craft.biddingSystem.services.AuctionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@Validated
@RequestMapping("/auction")
public class AuctionController {

    @Autowired
    AuctionService auctionService;

    @PostMapping("/create")
    public ResponseEntity<String> createAuction(@RequestBody @Valid AuctionRequest auctionRequest) {
        try {
            auctionService.createAuction(auctionRequest);
            return new ResponseEntity<>("Auction created", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error creating auction: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/updateAuctionDetails")
    public ResponseEntity<String> updateAuctionDetails(@RequestBody @Valid AuctionRequest auctionRequest) {
        try {
            auctionService.updateAuctionDetails(auctionRequest);
            return new ResponseEntity<>("Auction details updated.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error updating auction details: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/placeBid")
    public ResponseEntity<String> placeBid(@RequestParam String auctionId, @RequestParam String productId, @RequestParam String buyerId, @RequestParam double bidAmount) {
        try {
            auctionService.placeBid(auctionId, productId, buyerId, bidAmount);
            return new ResponseEntity<>("Bid placed successfully.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error placing bid: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/unsubscribe")
    public ResponseEntity<String> unsubscribe(@RequestBody Map<String, String> credentials) {
        try {
            auctionService.unsubscribe(credentials.get("auctionId"), credentials.get("buyerId"), ObserverType.BUYER);
            return new ResponseEntity<>("Unsubscribed successfully.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error unsubscribing: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}

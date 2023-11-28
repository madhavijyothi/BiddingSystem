package com.craft.biddingSystem.controllers;

import com.craft.biddingSystem.request.SellerRequest;
import com.craft.biddingSystem.services.SellerService;
import com.craft.biddingSystem.services.detailView.SellerViewAuctionDetails;
import com.craft.biddingSystem.response.AuctionDetailResponse;
import com.craft.biddingSystem.services.AuctionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;

@RestController
@Validated
@RequestMapping("/seller")
public class SellerController {

    @Autowired
    private SellerService sellerService;

    @Autowired
    private SellerViewAuctionDetails sellerViewAuctionDetails;

    @Autowired
    private AuctionService auctionService;

    @PostMapping("/register")
    public ResponseEntity<String> registerSeller(@RequestBody @Valid SellerRequest sellerRequest) {
        try {
            sellerService.register(sellerRequest);
            return new ResponseEntity<>("Registration is successful", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error in registration : " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginSeller(@RequestBody Map<String, String> credentials) {
        try {
            sellerService.authenticate(credentials.get("phoneNo"), credentials.get("password"));
            return new ResponseEntity<>("Authentication successful", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Authentication failed " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping("/auction/details")
    public AuctionDetailResponse getAuctionDetails(@RequestBody Map<String, String> credentials) {
        try {
            AuctionDetailResponse response = auctionService.getAuctionDetails(credentials.get("sellerId"), credentials.get("auctionId"), "SellerView");
            return response;
        } catch (Exception e) {
            return null;
        }
    }
}

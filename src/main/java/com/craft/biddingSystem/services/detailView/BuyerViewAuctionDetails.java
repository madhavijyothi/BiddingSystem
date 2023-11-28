package com.craft.biddingSystem.services.detailView;

import com.craft.biddingSystem.exception.BadRequestException;
import com.craft.biddingSystem.models.Auction;
import com.craft.biddingSystem.models.Product;
import com.craft.biddingSystem.repository.AuctionRepository;
import com.craft.biddingSystem.repository.ProductRepository;
import com.craft.biddingSystem.response.AuctionDetailResponse;
import org.springframework.stereotype.Service;


@Service
public class BuyerViewAuctionDetails extends AuctionDetails {


    private final AuctionRepository auctionRepository;

    private final ProductRepository productRepository;

    public BuyerViewAuctionDetails(AuctionRepository auctionRepository, ProductRepository productRepository) {
        this.auctionRepository = auctionRepository;
        this.productRepository = productRepository;
    }


    public AuctionDetailResponse getAuctionDetails(String buyerId, String auctionId) {
        if (buyerId == null || auctionId == null) {
            System.out.println("BuyerId or AuctionId is null");
            throw new BadRequestException("BuyerId or AuctionId is null");
        }

        try {
            Auction auction = auctionRepository.findByAuctionId(auctionId);
            Product product = productRepository.findByAuctionId(auctionId);
            AuctionDetailResponse auctionDetailResponse = new AuctionDetailResponse();
            auctionDetailResponse.setAuctionId(auction.getAuctionId());
            auctionDetailResponse.setAuctionState(auction.getAuctionState());
            auctionDetailResponse.setAuctionProductState(auction.getAuctionProductState());
            auctionDetailResponse.setBasePrice(auction.getBasePrice());
            auctionDetailResponse.setCurrentBid(auction.getCurrentBid());
            auctionDetailResponse.setProductId(product.getProductId());
            auctionDetailResponse.setProductName(product.getName());
            auctionDetailResponse.setProductDescription(product.getDescription());
            auctionDetailResponse.setCategory(product.getCategory());

            return auctionDetailResponse;
        } catch (Exception e) {
            System.out.println("No such auction exists for the buyer");
            throw new RuntimeException("No such auction exists for the buyer");
        }

    }
}


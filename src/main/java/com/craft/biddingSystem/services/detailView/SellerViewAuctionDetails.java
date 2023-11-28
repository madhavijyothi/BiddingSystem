package com.craft.biddingSystem.services.detailView;

import com.craft.biddingSystem.exception.AuthenticationException;
import com.craft.biddingSystem.repository.AuctionRepository;
import com.craft.biddingSystem.repository.ProductRepository;
import com.craft.biddingSystem.exception.BadRequestException;
import com.craft.biddingSystem.models.Auction;
import com.craft.biddingSystem.models.Product;
import com.craft.biddingSystem.response.AuctionDetailResponse;
import org.springframework.stereotype.Service;


@Service
public class SellerViewAuctionDetails extends AuctionDetails {


    private final AuctionRepository auctionRepository ;

    private final ProductRepository productRepository ;

    public SellerViewAuctionDetails(AuctionRepository auctionRepository, ProductRepository productRepository) {
        this.auctionRepository = auctionRepository;
        this.productRepository = productRepository;
    }



    public AuctionDetailResponse getAuctionDetails(String sellerId, String auctionId) {
        if(sellerId == null || auctionId == null) {
            System.out.println("SellerId or AuctionId is null");
            throw new BadRequestException("SellerId or AuctionId is null");
        }
        Auction auction = auctionRepository.findByAuctionId(auctionId);
        if (!auction.getSellerId().equals(sellerId)) {
            System.out.println("This auction does not belong to the seller who requested auction data.");
            throw new AuthenticationException("This auction does not belong to the seller who requested auction data.");
        }

        try{
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
        }catch (Exception e) {
            System.out.println("No such auction exists for the seller");
            throw new RuntimeException("No such auction exists for the seller");
        }

    }
}


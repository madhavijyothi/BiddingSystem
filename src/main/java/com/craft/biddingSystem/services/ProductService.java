package com.craft.biddingSystem.services;

import com.craft.biddingSystem.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    private final AuctionService auctionService;

    @Autowired
    public ProductService(ProductRepository productRepository, AuctionService auctionService) {
        this.productRepository = productRepository;
        this.auctionService = auctionService;
    }

}

package com.craft.biddingSystem.repository;

import com.craft.biddingSystem.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {


     Product findByProductId(String productId);
     Product findByAuctionId(String auctionId);
     List<Product> findByCategory(String category);



}

package com.craft.biddingSystem.repository;

import com.craft.biddingSystem.models.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SellerRepository extends JpaRepository<Seller,String> {

    Seller findBySellerId(String sellerId);
    Seller findBySellerPhoneNo(String phoneNo);
    Seller findBySellerEmailId(String emailId);
}

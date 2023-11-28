package com.craft.biddingSystem.repository;

import com.craft.biddingSystem.models.Buyer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BuyerRepository extends JpaRepository<Buyer,String> {

    Buyer findByBuyerId(String buyerId);
    Buyer findByBuyerPhoneNo(String phoneNo);

    Buyer findByBuyerEmailId(String emailId);

}

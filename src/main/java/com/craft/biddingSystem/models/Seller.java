package com.craft.biddingSystem.models;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "Seller")
public class Seller {

    @Id
    @Column(name = "seller_id")
    private String sellerId;
    @Column(name = "seller_name")
    private String sellerName;
    @Column(name = "seller_email_id")
    private String sellerEmailId;

    @Column(name = "seller_phone_no")
    private String sellerPhoneNo;
    @Column(name = "seller_password")
    private String sellerPassword;


}
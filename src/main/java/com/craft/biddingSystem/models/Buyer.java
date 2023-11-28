package com.craft.biddingSystem.models;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "Buyer")
public class Buyer {

    @Id
    @Column(name = "buyer_id")
    private String buyerId;
    @Column(name = "buyer_name")
    private String buyerName;
    @Column(name = "buyer_email_id")
    private String buyerEmailId;

    @Column(name = "buyer_phone_no")
    private String buyerPhoneNo;
    @Column(name = "buyer_password")
    private String buyerPassword;

}




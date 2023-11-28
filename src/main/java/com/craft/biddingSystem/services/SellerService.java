package com.craft.biddingSystem.services;


import com.craft.biddingSystem.exception.AuthenticationException;
import com.craft.biddingSystem.repository.AuctionRepository;
import com.craft.biddingSystem.repository.ProductRepository;
import com.craft.biddingSystem.repository.SellerRepository;
import com.craft.biddingSystem.request.SellerRequest;
import com.craft.biddingSystem.exception.BadRequestException;
import com.craft.biddingSystem.models.Event;
import com.craft.biddingSystem.models.Notification;
import com.craft.biddingSystem.models.Seller;
import com.craft.biddingSystem.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;


@Service
public class SellerService implements ObserverService {

    private final SellerRepository sellerRepository;
    private final AuctionRepository auctionRepository;
    private final ProductRepository productRepository;

    @Autowired
    public SellerService(SellerRepository sellerRepository, AuctionRepository auctionRepository, ProductRepository productRepository) {
        this.sellerRepository = sellerRepository;
        this.auctionRepository = auctionRepository;
        this.productRepository = productRepository;
    }

    private String generateId() {
        Random random = new Random();
        int randomInt = random.nextInt(100000);
        return String.valueOf(randomInt);

    }

    public Seller getData(String sellerId) {
        return sellerRepository.findBySellerId(sellerId);
    }

    private String generateSellerId() {
        return generateId() + ".SI";
    }

    public void register(SellerRequest sellerRequest) {

        boolean validPhoneNo = Utils.validatePhoneNo(sellerRequest.getPhoneNo());
        if (!validPhoneNo) {
            System.out.println("Please enter valid phoneNo.");
            throw new BadRequestException("Please enter valid phoneNo.");
        }

        try {
            boolean validEmailId = Utils.validateEmailId(sellerRequest.getEmailId());
            if (!validEmailId) {
                System.out.println("Please enter valid emailId.");
                throw new BadRequestException("Please enter valid emailId.");
            }
        } catch (Exception e) {
            System.out.println("Please enter valid emailId.");
        }

        if (sellerRepository.findBySellerPhoneNo(sellerRequest.getPhoneNo()) != null) {
            System.out.println("PhoneNo already exists.");
            throw new RuntimeException("PhoneNo already exists.");
        }

        if (sellerRepository.findBySellerEmailId(sellerRequest.getEmailId()) != null) {
            System.out.println("EmailId already exists.");
            throw new RuntimeException("EmailId already exists.");
        }

        String sellerId = this.generateSellerId();
        while (sellerRepository.findBySellerId(sellerId) != null) {
            sellerId = this.generateSellerId();
        }
        Seller seller = new Seller();
        seller.setSellerId(sellerId);
        seller.setSellerName(sellerRequest.getUsername());
        seller.setSellerEmailId(sellerRequest.getEmailId());
        seller.setSellerPassword(sellerRequest.getPassword());
        seller.setSellerPhoneNo(sellerRequest.getPhoneNo());

        sellerRepository.save(seller);
    }

    public void authenticate(String phoneNo, String password) {
        Seller seller = sellerRepository.findBySellerPhoneNo(phoneNo);

        if (seller != null && seller.getSellerPassword().equals(password)) {
            System.out.println("Authentication successful");
        } else {
           throw  new AuthenticationException("Invalid credentials");
        }
    }




    public boolean validateSeller(String sellerId) {
        return getData(sellerId) != null;
    }

    @Override
    public void updateObserver(List<String> sellerIds, Event event) {
        for (String sellerId : sellerIds) {
            boolean validate = this.validateSeller(sellerId);
            if (!validate) {
                continue;
            }
            Notification notification = new Notification(event.getMessage(), event.getDateTime());
            System.out.println("Notification to seller: " + notification.toString());
        }
    }

}

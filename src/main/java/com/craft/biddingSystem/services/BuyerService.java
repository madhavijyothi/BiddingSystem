package com.craft.biddingSystem.services;

import com.craft.biddingSystem.exception.AuthenticationException;
import com.craft.biddingSystem.exception.BadRequestException;
import com.craft.biddingSystem.exception.DuplicateEntityException;
import com.craft.biddingSystem.models.BidSubscribe;
import com.craft.biddingSystem.models.Buyer;
import com.craft.biddingSystem.models.Event;
import com.craft.biddingSystem.models.Notification;
import com.craft.biddingSystem.repository.AuctionRepository;
import com.craft.biddingSystem.repository.BidSubscribeRepository;
import com.craft.biddingSystem.repository.BuyerRepository;
import com.craft.biddingSystem.request.BuyerRequest;
import com.craft.biddingSystem.utils.Utils;
import com.craft.biddingSystem.response.BidDetails;
import com.craft.biddingSystem.response.BidListQueryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class BuyerService implements ObserverService {


    private final BuyerRepository buyerRepository;

    private final AuctionRepository auctionRepository;


    private final BidSubscribeRepository bidSubscribeRepository;

    @Autowired
    public BuyerService(BuyerRepository buyerRepository, BidSubscribeRepository bidSubscribeRepository, AuctionRepository auctionRepository) {
        this.buyerRepository = buyerRepository;
        this.bidSubscribeRepository = bidSubscribeRepository;
        this.auctionRepository = auctionRepository;
    }

    public static String generateId() {
        Random random = new Random();
        int randomInt = random.nextInt(100000);
        return String.valueOf(randomInt);
    }


    public Buyer getData(String buyerId) {
        return buyerRepository.findByBuyerId(buyerId);
    }

    public void register(BuyerRequest buyerRequest) {

        boolean validPhoneNo = Utils.validatePhoneNo(buyerRequest.getPhoneNo());
        if (!validPhoneNo) {
            System.out.println("Please enter valid phoneNo.");
            throw new BadRequestException("Please enter valid phoneNo.");
        }

        boolean validEmailId = Utils.validateEmailId(buyerRequest.getEmailId());
        if (!validEmailId) {
            System.out.println("Please enter valid emailId.");
            throw new BadRequestException("Please enter valid emailId.");
        }

        if (buyerRepository.findByBuyerPhoneNo(buyerRequest.getPhoneNo()) != null) {
            System.out.println("PhoneNo  already exists.");
            throw new DuplicateEntityException("PhoneNo already exists.");
        }

        if (buyerRepository.findByBuyerEmailId(buyerRequest.getEmailId()) != null) {
            System.out.println("EmailId  already exists.");
            throw new DuplicateEntityException("EmailId already exists.");
        }

        String buyerId = generateId() + ".BI";
        while(buyerRepository.findByBuyerId(buyerId) != null) {
            buyerId = generateId() + ".BI";
        }
        Buyer buyer = new Buyer();
        buyer.setBuyerId(buyerId);
        buyer.setBuyerName(buyerRequest.getUsername());
        buyer.setBuyerEmailId(buyerRequest.getEmailId());
        buyer.setBuyerPhoneNo(buyerRequest.getPhoneNo());
        buyer.setBuyerPassword(buyerRequest.getPassword());
        buyerRepository.save(buyer);
    }


    public Buyer authenticate(String phoneNo, String password) {
        Buyer buyer = buyerRepository.findByBuyerPhoneNo(phoneNo);
        if (buyer != null && buyer.getBuyerPassword().equals(password)) {
            return buyer;
        } else {
            throw new AuthenticationException("Invalid credentials");
        }
    }

    public List<BidSubscribe> getBidSubscribesByBuyer(String buyerId) {
        return bidSubscribeRepository.findByBuyerId(buyerId);
    }


    public BidListQueryResponse viewAllBuyerBids(String buyerId) {
        boolean validate = this.validateBuyer(buyerId);
        if (!validate) {
            System.out.println("Buyer Id not valid");
            throw new BadRequestException("Invalid buyerId");
        }
        List<BidSubscribe> bidSubscribes = bidSubscribeRepository.findByBuyerId(buyerId);
        BidListQueryResponse response = new BidListQueryResponse();
        List<BidDetails> bidDetailsList = new ArrayList<>();

        for (BidSubscribe bidSubscribe : bidSubscribes) {
            BidDetails data = new BidDetails();
            data.setAuctionId(bidSubscribe.getAuctionId());
            data.setBidAmount(bidSubscribe.getBidPrice());
            bidDetailsList.add(data);
        }
        response.setBuyerBidDetailsList(bidDetailsList);
        return response;
    }

    public boolean validateBuyer(String buyerId) {
        return buyerRepository.findByBuyerId(buyerId) != null;
    }

    @Override
    public void updateObserver(List<String> buyerIds, Event event) {
        for (String buyerId : buyerIds) {
            boolean validate = this.validateBuyer(buyerId);
            if (!validate) {
                continue;
            }
            Notification notification = new Notification(event.getMessage(), event.getDateTime());
            System.out.println("Notification sent to Buyer : " + notification.toString());
        }
    }
}
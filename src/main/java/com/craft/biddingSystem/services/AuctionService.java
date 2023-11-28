package com.craft.biddingSystem.services;

import com.craft.biddingSystem.enums.AuctionProductState;
import com.craft.biddingSystem.enums.AuctionState;
import com.craft.biddingSystem.enums.ObserverType;
import com.craft.biddingSystem.enums.PublisherEventType;
import com.craft.biddingSystem.exception.BadRequestException;
import com.craft.biddingSystem.exception.CustomException;
import com.craft.biddingSystem.exception.InValidAuctionState;
import com.craft.biddingSystem.models.Auction;
import com.craft.biddingSystem.models.BidSubscribe;
import com.craft.biddingSystem.models.Event;
import com.craft.biddingSystem.models.Product;
import com.craft.biddingSystem.repository.AuctionRepository;
import com.craft.biddingSystem.repository.BidSubscribeRepository;
import com.craft.biddingSystem.repository.ProductRepository;
import com.craft.biddingSystem.request.AuctionRequest;
import com.craft.biddingSystem.services.detailView.AuctionDetails;
import com.craft.biddingSystem.services.detailView.BuyerViewAuctionDetails;
import com.craft.biddingSystem.services.detailView.SellerViewAuctionDetails;
import com.craft.biddingSystem.response.AuctionDetailResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class AuctionService implements PublisherService {
    private final AuctionRepository auctionRepository;

    private final ProductRepository productRepository;

    private final BuyerService buyerService;
    private final SellerService sellerService;

    private final BidSubscribeRepository bidSubscribeRepository;

    @Autowired
    public AuctionService(AuctionRepository auctionRepository, BuyerService buyerService, SellerService sellerService, ProductRepository productRepository, BidSubscribeRepository bidSubscribeRepository) {
        this.auctionRepository = auctionRepository;
        this.buyerService = buyerService;
        this.sellerService = sellerService;
        this.productRepository = productRepository;
        this.bidSubscribeRepository = bidSubscribeRepository;
    }

    private String generateId() {
        Random random = new Random();
        int randomInt = random.nextInt(100000);
        return String.valueOf(randomInt);

    }

    private boolean validateSeller(String sellerId) {
        return sellerService.validateSeller(sellerId);
    }

    private boolean validateBuyer(String buyerId) {
        return buyerService.validateBuyer(buyerId);
    }

    public Auction getData(String auctionId) {
        return auctionRepository.findByAuctionId(auctionId);
    }

    public void createAuction(AuctionRequest auctionRequest) {
        boolean validate = this.validateSeller(auctionRequest.getSellerId());
        if (!validate) {
            System.out.println("Seller Id is invalid");
            throw new BadRequestException("Seller Id is invalid");
        }
        Auction auction = new Auction();
        String auctionId = generateId() + ".AI";
        String productId = generateId() + ".PI";
        while (auctionRepository.findByAuctionId(auctionId) != null) {
            auctionId = generateId() + ".AI";
        }
        while (productRepository.findByProductId(productId) != null) {
            productId = generateId() + ".PI";
        }

        auction.setAuctionId(auctionId);
        if (auctionRequest.getAuctionState() == AuctionState.DRAFTED || auctionRequest.getAuctionState() == AuctionState.SAVED)
            auction.setAuctionState(auctionRequest.getAuctionState());
        else {
            System.out.println("Auction state is invalid");
            throw new InValidAuctionState("Auction state is invalid");
        }
        auction.setSellerId(auctionRequest.getSellerId());
        auction.setBasePrice(auctionRequest.getBasePrice());
        auction.setAuctionProductState(AuctionProductState.UNBID);
        auction.setCreatedDate(new Date());
        auction.setModifiedDate(new Date());
        auction.setStartDate(auctionRequest.getStartDate());
        auction.setEndDate(auctionRequest.getEndDate());

        auctionRepository.save(auction);

        Product product = new Product();
        product.setProductId(productId);
        product.setName(auctionRequest.getProductName());
        product.setDescription(auctionRequest.getProductDescription());
        product.setAuctionId(auction.getAuctionId());
        product.setCategory(auctionRequest.getCategory());

        productRepository.save(product);
        System.out.println("AuctionId  created ");
    }


    public void updateAuctionDetails(AuctionRequest auctionRequest) {
        boolean validate = this.validateSeller(auctionRequest.getSellerId());
        if (!validate) {
            System.out.println("Seller Id is invalid");
            throw new BadRequestException("Seller Id is invalid");
        }
        if (auctionRequest.getAuctionId() == null) {
            System.out.println("Auction Id is null");
            throw new BadRequestException("Auction Id is null");
        }
        String auctionId = auctionRequest.getAuctionId();

        Auction auction = auctionRepository.findByAuctionId(auctionId);
        if (auction == null) {
            System.out.println("Auction Id does not exist");
            throw new BadRequestException("Auction Id does not exist");
        }
        if (auction.getAuctionState() != AuctionState.DRAFTED) {
            System.out.println("Auction is  not in  DRAFT state. Cannot update the auction details.");
            throw new CustomException("Auction is not  in DRAFT state. Cannot update the auction details.");
        }
        auction.setBasePrice(auctionRequest.getBasePrice());
        auction.setAuctionState(auctionRequest.getAuctionState());
        auction.setModifiedDate(new Date());

        auctionRepository.save(auction);

        Product product = productRepository.findByAuctionId(auctionId);
        if (auctionRequest.getProductName() != null)
            product.setName(auctionRequest.getProductName());
        if (auctionRequest.getProductDescription() != null)
            product.setDescription(auctionRequest.getProductDescription());
          product.setCategory(auctionRequest.getCategory());
        productRepository.save(product);

        System.out.println("Auction Details updated . ");
    }

    public void subscribeToAuction(String buyerId, String auctionId, double bidPrice) {
        boolean validate = this.validateBuyer(buyerId);
        if (!validate) {
            System.out.println("Buyer Id not valid");
            return;
        }
        System.out.println("subscribeToAuction : auctionId + buyerId " + auctionId + buyerId);
        BidSubscribe bidSubscribe = bidSubscribeRepository.findByBidSubscribeId(auctionId + buyerId);
        System.out.println("bidSubscribe : " + bidSubscribe);
        if (bidSubscribe != null && bidSubscribe.getAuctionId() != null && bidSubscribe.getAuctionId().equals(auctionId)) {
            bidSubscribe.setBidPrice(bidPrice);
            bidSubscribe.setModifiedDate(new Date());
            System.out.println("Already subscribed to the auction.");
            return;
        } else {
            bidSubscribe = new BidSubscribe(auctionId + buyerId, auctionId, buyerId, bidPrice, new Date());
            bidSubscribe.setIsSubscribed(1);
        }

        bidSubscribeRepository.save(bidSubscribe);

    }


    public void startAuction(Auction auction, String auctionId) {
        boolean validate = this.validateAuction(auctionId);
        if (!validate) {
            System.out.println("Auction Id is invalid");
            return;
        }
        System.out.println("start Auction : ");
        auction.setAuctionState(AuctionState.STARTED);
        auctionRepository.save(auction);
        updateObserversAboutAuctionStarted(auction);
    }

    public void endAuction(Auction auction) {
        boolean validate = this.validateAuction(auction.getAuctionId());
        if (!validate) {
            System.out.println("Auction Id is invalid");
            return;
        }
        if (auction.getAuctionProductState().equals(AuctionProductState.UNBID)) {
            auction.setAuctionProductState(AuctionProductState.FAILED);
            auction.setAuctionState(AuctionState.END_FAILED);
        } else if (auction.getCurrentBid() >= auction.getBasePrice()) {
            auction.setAuctionProductState(AuctionProductState.SOLD);
        } else {
            auction.setAuctionProductState(AuctionProductState.FAILED);
            auction.setAuctionState(AuctionState.END_FAILED);
        }

        if (!auction.getAuctionState().equals(AuctionState.END_FAILED)) {
            auction.setAuctionState(AuctionState.END_SUCCESS);
        }
        auctionRepository.save(auction);
        System.out.println("Auction status end : " + auction.getAuctionState());
        updateObserversAboutAuctionEnded(auction);
    }

    public void placeBid(String auctionId, String productId, String buyerId, double bidValue) {
        boolean validate = this.validateAuction(auctionId);
        if (!validate) {
            System.out.println("Auction Id is invalid");
            throw new RuntimeException("Auction Id is invalid");

        }
        validate = this.validateBuyer(buyerId);
        if (!validate) {
            System.out.println("Buyer Id is invalid");
            throw new RuntimeException("Buyer Id is invalid");
        }

        Auction auction = auctionRepository.findByAuctionId(auctionId);
        Product product = productRepository.findByAuctionId(auctionId);


        if (!product.getProductId().equals(productId)) {
            System.out.println("productId given does not belong to this auction.");
            throw new RuntimeException("productId given does not belong to this auction.");
        }


        if (auction.getAuctionState() == AuctionState.STARTED &&
                auction.getAuctionProductState() != AuctionProductState.SOLD &&
                auction.getAuctionProductState() != AuctionProductState.FAILED) {
            if (auction.getCurrentBid() != null && bidValue <= auction.getCurrentBid()) {
                System.out.println("Bid Value should be greater than current bid price.");
                throw new RuntimeException("Bid Value should be greater than current bid price.");

            }
            subscribeToAuction(buyerId, auctionId, bidValue);
            auction.setAuctionProductState(AuctionProductState.BID);
            auction.setCurrentBid(bidValue);
            auction.setCurrentWinningBuyerId(buyerId);
            auction.setModifiedDate(new Date());

            auctionRepository.save(auction);
            updateObserverAboutNewBidPrice(auction, bidValue);
        } else {
            System.out.println("Either auction is not started or auction is ended or product is sold.");
            throw new RuntimeException("Either auction is not started or auction is ended or product is sold.");
        }
    }


    private void updateObserversAboutAuctionStarted(Auction auction) {
        Event event = new Event();
        event.setEventType(PublisherEventType.AUCTION_STARTED);
        event.setMessage("Auction Started! - " + auction.getAuctionId());
        event.setDateTime(new Date());

        updateObservers(Collections.singletonList(auction.getSellerId()), ObserverType.SELLER, event);

    }

    private void updateObserversAboutAuctionEnded(Auction auction) {
        Event event;

        event = new Event();
        event.setEventType(PublisherEventType.AUCTION_ENDED);
        event.setMessage("Auction Ended! - " + auction.getAuctionId());
        event.setDateTime(new Date());

        System.out.println("Auction Ended! -  " + auction.getAuctionId());

        List<BidSubscribe> bidSubscribes = bidSubscribeRepository.findByAuctionIdAndIsSubscribed(auction.getAuctionId(), 1);
        List<String> observerIds = new ArrayList<>();
        for (BidSubscribe bidSubscribe : bidSubscribes) {
            observerIds.add(bidSubscribe.getBuyerId());
        }

        updateObservers(observerIds, ObserverType.BUYER, event);

        Product product = productRepository.findByAuctionId(auction.getAuctionId());

        event = new Event();
        event.setEventType(PublisherEventType.AUCTION_ENDED);
        event.setMessage("Auction Ended! - " + auction.getAuctionId()
                + "Congrats! You bought the product - " + product.getProductId() + " at price - " + auction.getCurrentBid());
        event.setDateTime(new Date());
        updateObservers(Collections.singletonList(auction.getCurrentWinningBuyerId()), ObserverType.BUYER, event);
        updateObservers(Collections.singletonList(auction.getSellerId()), ObserverType.SELLER, event);
    }


    private void updateObserverAboutNewBidPrice(Auction auction, double bidValue) {
        Event event = new Event();
        event.setEventType(PublisherEventType.BID_VALUE_UPDATED);
        event.setMessage("Bid Price updated! Auction - " + auction.getAuctionId() + "New Bid price is: " + bidValue);
        event.setDateTime(new Date());
        List<BidSubscribe> bidSubscribes = bidSubscribeRepository.findByAuctionIdAndIsSubscribed(auction.getAuctionId(), 1);
        List<String> observerIds = new ArrayList<>();
        for (BidSubscribe bidSubscribe : bidSubscribes) {
            observerIds.add(bidSubscribe.getBuyerId());
        }

        updateObservers(observerIds, ObserverType.BUYER, event);
    }

    @Override
    public void updateObservers(List<String> observerIds, ObserverType observerType, Event event) {
        ObserverService observerService;
        switch (observerType) {
            case BUYER:
                buyerService.updateObserver(observerIds, event);
                break;
            case SELLER:
                sellerService.updateObserver(observerIds, event);
                break;
            default:
                System.out.println("Observer Type not listed");
        }
    }

    public AuctionDetailResponse getAuctionDetails(String userId, String auctionId, String algorithm) {

        AuctionDetails auctionDetailsObject = null;

        if (algorithm.equals("SellerView")) {
            auctionDetailsObject = new SellerViewAuctionDetails(auctionRepository, productRepository);
        } else if (algorithm.equals("BuyerView")) {
            BidSubscribe bidSubscribe = bidSubscribeRepository.findByBidSubscribeId(auctionId + userId);
            if (bidSubscribe == null) {
                System.out.println("Buyer is not subscribed to the auction");
                throw new RuntimeException("Buyer is not subscribed to the auction");
            }
            auctionDetailsObject = new BuyerViewAuctionDetails(auctionRepository, productRepository);
        } else {
            System.out.println("Invalid algorithm");
            return null;
        }

        return auctionDetailsObject.getAuctionDetails(userId, auctionId);
    }


    public boolean validateAuction(String auctionId) {
        return getData(auctionId) != null;
    }

    public void unsubscribe(String auctionId, String observerId, ObserverType type) {
        boolean validate = this.validateAuction(auctionId);
        if (!validate) {
            System.out.println("Auction Id is invalid");
            throw new BadRequestException("Auction Id is invalid");
        }
        if (type.equals(ObserverType.valueOf("SELLER"))) {
            System.out.println("Seller not allowed to unsubscribe his auction");
            throw new BadRequestException("Seller not allowed to unsubscribe his auction");
        }
        Auction auction = getData(auctionId);
        System.out.println("unsubscribe : auctionId + observerId " + auctionId + observerId);
        if (type.equals(ObserverType.valueOf("BUYER"))) {
            boolean eligible = eligibleToUnsubscribe(auction, observerId);
            if (!eligible) {
                throw new RuntimeException("Buyer is current winning bidder for one of the product and so not allowed to unsubscribe.");
            }
        }
        BidSubscribe bidSubscribe = bidSubscribeRepository.findByBidSubscribeId(auctionId + observerId);
        if(bidSubscribe == null){
            throw new RuntimeException("Buyer is not subscribed to the auction");
        }
        bidSubscribe.setIsSubscribed(0);
        bidSubscribe.setModifiedDate(new Date());
        bidSubscribeRepository.save(bidSubscribe);
    }

    private boolean eligibleToUnsubscribe(Auction auction, String observerId) {

        if (auction.getCurrentWinningBuyerId().equals(observerId)) {
            System.out.println("Buyer is current winning bidder for one of the product and so not allowed to unsubscribe.");
            return false;
        }
        return true;
    }

    private static Date convertStringToDate(String dateString, String pattern, String timeZoneId) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        dateFormat.setTimeZone(TimeZone.getTimeZone(timeZoneId));

        try {
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}

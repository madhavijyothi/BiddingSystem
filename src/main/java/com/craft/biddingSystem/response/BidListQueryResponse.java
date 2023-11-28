package com.craft.biddingSystem.response;

import java.util.List;

public class BidListQueryResponse {
        List<BidDetails> buyerBidDetailsList;

        public List<BidDetails> getBuyerBidDetailsList() {
                return buyerBidDetailsList;
        }

        public void setBuyerBidDetailsList(List<BidDetails> buyerBidDetailsList) {
                this.buyerBidDetailsList = buyerBidDetailsList;
        }
}

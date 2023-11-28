package com.craft.biddingSystem.request;

import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

// Class for storing the data about new bet entered by the user
public class BetRequest {

    @PositiveOrZero
    private BigDecimal amount;

    public BetRequest() {
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}

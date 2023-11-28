package com.craft.biddingSystem.request;


import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.time.LocalDateTime;

// Class for storing the data about new lot entered by the user
public class LotRequest {

    @NotBlank(message = "Please, enter title")
    private String title;
    @NotBlank(message = "Please, enter description")
    private String description;
    @Future(message = "There must be a date in the future")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @NotNull(message = "Please, enter end date")
    private LocalDateTime endDate;
    @PositiveOrZero(message = "Cost cannot be less than zero")
    private BigDecimal initCost;

    public LotRequest() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getInitCost() {
        return initCost;
    }

    public void setInitCost(BigDecimal initCost) {
        this.initCost = initCost;
    }
}

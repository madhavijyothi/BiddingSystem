package com.craft.biddingSystem.models;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Lot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String title;
    @Column(columnDefinition = "text")
    private String description;
    @Column(updatable = false)
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private BigDecimal initCost;
    private boolean active;
    private boolean archival;

    @ManyToOne(fetch = FetchType.EAGER)
    private User user;
    @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER, mappedBy = "lot", orphanRemoval = true)
    private List<Bet> bets = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        this.startDate = LocalDateTime.now();
    }

    public Lot() {
    }

    public Lot(String title, String description, LocalDateTime startDate, LocalDateTime endDate, BigDecimal initCost, boolean active, boolean archival, List<Bet> bets) {
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.initCost = initCost;
        this.active = active;
        this.archival = archival;
        this.bets = bets;
    }

    public Lot(Long id, String title, String description, LocalDateTime startDate, LocalDateTime endDate, BigDecimal initCost, boolean active, boolean archival, User user, List<Bet> bets) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.initCost = initCost;
        this.active = active;
        this.archival = archival;
        this.user = user;
        this.bets = bets;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Bet> getBets() {
        return bets;
    }

    public void setBets(List<Bet> bets) {
        this.bets = bets;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isArchival() {
        return archival;
    }

    public void setArchival(boolean archival) {
        this.archival = archival;
    }
}

package com.craft.biddingSystem.services;


import com.craft.biddingSystem.exception.LotCreateException;
import com.craft.biddingSystem.exception.LotDeleteException;
import com.craft.biddingSystem.exception.LotNotFoundException;
import com.craft.biddingSystem.models.Bet;
import com.craft.biddingSystem.models.Lot;
import com.craft.biddingSystem.models.User;
import com.craft.biddingSystem.models.enums.Role;
import com.craft.biddingSystem.repository.BetRepository;
import com.craft.biddingSystem.repository.LotRepository;
import com.craft.biddingSystem.repository.UserRepository;
import com.craft.biddingSystem.request.LotRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

// Class that implements the business logic of the lots
@Service
public class LotService {

    Logger logger = LoggerFactory.getLogger(getClass());

    private LotRepository lotRepository;
    private UserRepository userRepository;
    private BetRepository betRepository;

    @Autowired
    public LotService(LotRepository lotRepository, UserRepository userRepository, BetRepository betRepository) {
        this.lotRepository = lotRepository;
        this.userRepository = userRepository;
        this.betRepository = betRepository;
    }

    public Lot createLot(LotRequest lotRequest, Principal principal) {
        User user = getUserByPrincipal(principal);

        Lot lot = new Lot();
        lot.setTitle(lotRequest.getTitle());
        lot.setDescription(lotRequest.getDescription());
        lot.setInitCost(lotRequest.getInitCost());

        // Check if start date is after end date
        if (LocalDateTime.now().isBefore(lotRequest.getEndDate())) {
            lot.setStartDate(LocalDateTime.now());
            lot.setEndDate(lotRequest.getEndDate());
        } else {
            throw new LotCreateException("Start date must be after end date");
        }

        lot.setBets(null);
        lot.setUser(user);
        lot.setActive(true);
        lot.setArchival(false);

        return lotRepository.save(lot);
    }

    public List<Lot> saveLots(List<Lot> lots){
        return lotRepository.saveAll(lots);
    }

    public List<Lot> getAllLots(boolean isActive) {
        return lotRepository.findAllByActiveAndArchivalIsFalse(isActive);
    }

    public Lot getLotById(Long lotId) {
        return lotRepository.findByIdAndArchivalIsFalse(lotId)
                .orElseThrow(() -> new LotNotFoundException("Lot cannot be found"));
    }

    public List<Lot> getAllLotsForUser(Principal principal, boolean active) {
        User user = getUserByPrincipal(principal);
        return lotRepository.findAllByUserAndActiveAndArchivalIsFalseOrderByStartDateDesc(user, active);
    }

    public List<Lot> getLotsUserParticipateIn(Principal principal, boolean active){
        User user = getUserByPrincipal(principal);
        return lotRepository.findDistinctLotByUserIdAndArchivalIsFalse(user.getId()).stream()
                .filter(lot -> lot.isActive() == active)
                .collect(Collectors.toList());
    }

    public List<Lot> getFirst10Lots(){
        return lotRepository.findFirst10ByActiveIsTrueAndArchivalIsFalseOrderByStartDateDesc();
    }

    public void deleteLot(Long lotId, Principal principal) {
        User currUser = getUserByPrincipal(principal);
        Lot lot = lotRepository.findByIdAndArchivalIsFalse(lotId)
                .orElseThrow(()->new LotNotFoundException("Lot with id " + lotId + " cannot be found"));

        if(!lot.isActive()){
            throw new LotDeleteException("Lot isn't active");
        }

        // Check deletion rights
        if(lot.getUser().equals(currUser) || currUser.getRoles().contains(Role.ROLE_ADMIN)) {
            lot.setArchival(true);
            List<Bet> modifiedBets = new ArrayList<>();
            lot.getBets()
                    .forEach(bet -> {
                        bet.setArchival(true);
                        modifiedBets.add(bet);
                    });
            betRepository.saveAll(modifiedBets);
            lotRepository.save(lot);
        }
        else {
            throw new AccessDeniedException("Only admin can delete data that is not his");
        }
    }

    public List<Lot> getLotsByKeyword(String keyword){
        return lotRepository.findAllByTitleContainsAndArchivalIsFalse(keyword);
    }


    private User getUserByPrincipal(Principal principal) {
        String name = principal.getName();
        return userRepository.findUserByUsername(name)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with name " + name));
    }
}

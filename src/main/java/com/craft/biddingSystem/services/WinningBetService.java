package com.craft.biddingSystem.services;

import com.craft.biddingSystem.models.User;
import com.craft.biddingSystem.models.WinningBet;
import com.craft.biddingSystem.repository.LotRepository;
import com.craft.biddingSystem.repository.UserRepository;
import com.craft.biddingSystem.repository.WinningBetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
public class WinningBetService {

    private WinningBetRepository winningBetRepository;
    private UserRepository userRepository;
    private LotRepository lotRepository;

    @Autowired
    public WinningBetService(WinningBetRepository winningBetRepository, UserRepository userRepository, LotRepository lotRepository) {
        this.winningBetRepository = winningBetRepository;
        this.userRepository = userRepository;
        this.lotRepository = lotRepository;
    }

    public List<WinningBet> saveWinningBets(List<WinningBet> winningBets) {
        return winningBetRepository.saveAll(winningBets);
    }

    public List<WinningBet> getAllWinningBetsForUser(Principal principal) {
        User user = getUserByPrincipal(principal);

        return winningBetRepository.findAllByBetUserId(user.getId());
    }

    private User getUserByPrincipal(Principal principal) {
        String name = principal.getName();
        return userRepository.findUserByUsername(name)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with name " + name));
    }
}

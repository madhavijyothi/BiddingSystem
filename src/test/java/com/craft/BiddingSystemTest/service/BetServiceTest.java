package com.craft.BiddingSystemTest.service;

import com.craft.biddingSystem.exception.BetCreateException;
import com.craft.biddingSystem.exception.BetDeleteException;
import com.craft.biddingSystem.exception.BetNotFoundException;
import com.craft.biddingSystem.exception.LotNotFoundException;
import com.craft.biddingSystem.models.Bet;
import com.craft.biddingSystem.models.Lot;
import com.craft.biddingSystem.models.User;
import com.craft.biddingSystem.models.enums.Role;
import com.craft.biddingSystem.repository.BetRepository;
import com.craft.biddingSystem.repository.LotRepository;
import com.craft.biddingSystem.repository.UserRepository;
import com.craft.biddingSystem.request.BetRequest;
import com.craft.biddingSystem.services.BetService;
import com.sun.security.auth.UserPrincipal;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.access.AccessDeniedException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@SpringBootTest
public class BetServiceTest {

    @Autowired
    BetService betService;

    @MockBean
    private BetRepository betRepository;
    @MockBean
    private LotRepository lotRepository;
    @MockBean
    private UserRepository userRepository;


    @Test
    public void createBet_LotIsNull_ShouldThrowException() {
        Mockito.when(userRepository.findUserByUsername(ArgumentMatchers.anyString())).
                thenReturn(Optional.of(new User()));
        Mockito.when(lotRepository.findByIdAndArchivalIsFalse(ArgumentMatchers.anyLong())).
                thenReturn(Optional.empty());

        Throwable throwable = Assertions.assertThrows(LotNotFoundException.class,
                () -> betService.createBet((long) 1, new BetRequest(), new UserPrincipal("user")));
        Assertions.assertNotNull(throwable);
    }

    @Test
    public void createBet_LotIsNotActive_ShouldThrowException() {
        Mockito.when(userRepository.findUserByUsername(ArgumentMatchers.anyString())).
                thenReturn(Optional.of(new User()));
        Lot lot = new Lot("", "", LocalDateTime.now(), LocalDateTime.now().plusMinutes(10),
                BigDecimal.ZERO, false, false, new ArrayList<>());
        Mockito.when(lotRepository.findByIdAndArchivalIsFalse(ArgumentMatchers.anyLong())).
                thenReturn(Optional.of(lot));

        Throwable throwable = Assertions.assertThrows(BetCreateException.class,
                () -> betService.createBet((long) 1, new BetRequest(), new UserPrincipal("user")));
        Assertions.assertNotNull(throwable);
    }

    @Test
    public void createBet_BetCanBeCreated_ShouldCreateBet() {
        Mockito.when(userRepository.findUserByUsername(ArgumentMatchers.anyString())).
                thenReturn(Optional.of(new User()));
        Lot lot = new Lot("", "", LocalDateTime.now(), LocalDateTime.now().plusMinutes(10),
                BigDecimal.ZERO, true, false, new ArrayList<>());
        lot.setUser(new User());
        Mockito.when(lotRepository.findByIdAndArchivalIsFalse(ArgumentMatchers.anyLong())).
                thenReturn(Optional.of(lot));

        BetRequest betRequest = new BetRequest();
        betRequest.setAmount(BigDecimal.ONE);

        betService.createBet((long) 1, betRequest, new UserPrincipal("user"));

        Mockito.verify(betRepository, Mockito.times(1)).save(ArgumentMatchers.any());
    }

    @Test
    public void deleteBet_BetIsNull_ShouldThrowException() {
        Mockito.when(userRepository.findUserByUsername(ArgumentMatchers.anyString())).
                thenReturn(Optional.of(new User()));
        Mockito.when(betRepository.findByIdAndArchivalIsFalse(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.empty());

        Throwable throwable = Assertions.assertThrows(BetNotFoundException.class,
                () -> betService.deleteBet((long) 1, new UserPrincipal("user")));
        Assertions.assertNotNull(throwable);
    }

    @Test
    public void deleteBet_LotIsNotActive_ShouldThrowException() {
        Mockito.when(userRepository.findUserByUsername(ArgumentMatchers.anyString())).
                thenReturn(Optional.of(new User()));

        Lot lot = new Lot("", "", LocalDateTime.now(), LocalDateTime.now().plusMinutes(10),
                BigDecimal.ZERO, false, false, new ArrayList<>());
        Bet bet = new Bet((long) 1, lot, (long) 1, BigDecimal.ONE, LocalDateTime.now(), false);

        Mockito.when(betRepository.findByIdAndArchivalIsFalse(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(bet));


        Throwable throwable = Assertions.assertThrows(BetDeleteException.class,
                () -> betService.deleteBet((long) 1, new UserPrincipal("user")));
        Assertions.assertNotNull(throwable);
    }

    @Test
    public void deleteBet_UserHasNotAccess_ShouldThrowException() {
        User currUser = new User();
        currUser.setId((long) 10);
        currUser.setEmail("");
        currUser.setPassword("");
        currUser.setRoles(new HashSet<>());

        Mockito.when(userRepository.findUserByUsername(ArgumentMatchers.anyString())).
                thenReturn(Optional.of(currUser));

        Lot lot = new Lot("", "", LocalDateTime.now(), LocalDateTime.now().plusMinutes(10),
                BigDecimal.ZERO, true, false, new ArrayList<>());
        Bet bet = new Bet((long) 1, lot, (long) 1, BigDecimal.ONE, LocalDateTime.now(), false);

        Mockito.when(betRepository.findByIdAndArchivalIsFalse(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(bet));


        Throwable throwable = Assertions.assertThrows(AccessDeniedException.class,
                () -> betService.deleteBet((long) 1, new UserPrincipal("user")));
        Assertions.assertNotNull(throwable);
    }

    @Test
    public void deleteBet_UserHasAccess_ShouldPerformDeletion() {
        User currUser = new User();
        currUser.setId((long) 10);
        currUser.setEmail("");
        currUser.setPassword("");
        Set<Role> roles = new HashSet<>();
        roles.add(Role.ROLE_USER);
        roles.add(Role.ROLE_ADMIN);
        currUser.setRoles(roles);

        Mockito.when(userRepository.findUserByUsername(ArgumentMatchers.anyString())).
                thenReturn(Optional.of(currUser));
        Lot lot = new Lot("", "", LocalDateTime.now(), LocalDateTime.now().plusMinutes(10),
                BigDecimal.ZERO, true, false, new ArrayList<>());
        Bet bet = new Bet((long) 1, lot, (long) 10, BigDecimal.ONE, LocalDateTime.now(), false);
        Mockito.when(betRepository.findByIdAndArchivalIsFalse(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(bet));

        betService.deleteBet((long) 1, new UserPrincipal("user"));

        bet.setArchival(true);
        Mockito.verify(betRepository, Mockito.times(1)).save(bet);
    }

    @Test
    public void getAllBetsForLot_LotIsNull_ShouldThrowException(){
        Mockito.when(lotRepository.findByIdAndArchivalIsFalse(ArgumentMatchers.anyLong())).
                thenReturn(Optional.empty());

        Throwable throwable = Assertions.assertThrows(LotNotFoundException.class,
                () -> betService.getAllBetsForLot((long) 1));
        Assertions.assertNotNull(throwable);
    }

    @Test
    public void getAllBetsForLot_LotIsNotNull_ShouldReturnBetsList(){
        Lot lot = new Lot("", "", LocalDateTime.now(), LocalDateTime.now().plusMinutes(10),
                BigDecimal.ZERO, true, false, new ArrayList<>());

        Mockito.when(lotRepository.findByIdAndArchivalIsFalse(ArgumentMatchers.anyLong())).
                thenReturn(Optional.of(lot));
        Mockito.when(betRepository.findAllByLotAndArchivalIsFalse(ArgumentMatchers.any())).
                thenReturn(new ArrayList<>());

        Assertions.assertNotNull(betService.getAllBetsForLot((long) 1));
    }

    @Test
    public void getBetById_BetIsNull_ShouldThrowException(){
        Mockito.when(betRepository.findByIdAndArchivalIsFalse(ArgumentMatchers.anyLong())).
                thenReturn(Optional.empty());

        Throwable throwable = Assertions.assertThrows(BetNotFoundException.class,
                () -> betService.getBetById((long) 1));
        Assertions.assertNotNull(throwable);
    }

    @Test
    public void getBetById_BetIsNotNull_ShouldReturnBet(){
        Bet bet = new Bet((long) 1, null, (long) 10, BigDecimal.ONE, LocalDateTime.now(), false);

        Mockito.when(betRepository.findByIdAndArchivalIsFalse(ArgumentMatchers.anyLong())).
                thenReturn(Optional.of(bet));

        Assertions.assertEquals(bet, betService.getBetById((long) 1));
    }
}

package com.craft.BiddingSystemTest.service;

import com.craft.biddingSystem.exception.LotCreateException;
import com.craft.biddingSystem.exception.LotDeleteException;
import com.craft.biddingSystem.exception.LotNotFoundException;
import com.craft.biddingSystem.models.Lot;
import com.craft.biddingSystem.models.User;
import com.craft.biddingSystem.models.enums.Role;
import com.craft.biddingSystem.repository.BetRepository;
import com.craft.biddingSystem.repository.LotRepository;
import com.craft.biddingSystem.repository.UserRepository;
import com.craft.biddingSystem.request.LotRequest;
import com.craft.biddingSystem.services.LotService;
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
public class LotServiceTest {

    @Autowired
    LotService lotService;

    @MockBean
    private LotRepository lotRepository;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private BetRepository betRepository;

    @Test
    public void createLot_EndDateBeforeCurrDate_ShouldThrowException(){
        LotRequest lotRequest = new LotRequest();
        lotRequest.setTitle("");
        lotRequest.setDescription("");
        lotRequest.setInitCost(BigDecimal.ONE);
        lotRequest.setEndDate(LocalDateTime.now().minusDays(1));
        Mockito.when(userRepository.findUserByUsername(ArgumentMatchers.anyString())).
                thenReturn(Optional.of(new User()));

        Throwable throwable = Assertions.assertThrows(LotCreateException.class,
                () -> lotService.createLot(lotRequest, new UserPrincipal("user")));
        Assertions.assertNotNull(throwable);
    }

    @Test
    public void createLot_EndDateAfterCurrDate_ShouldReturnLot(){
        LotRequest lotRequest = new LotRequest();
        lotRequest.setTitle("");
        lotRequest.setDescription("");
        lotRequest.setInitCost(BigDecimal.ONE);
        lotRequest.setEndDate(LocalDateTime.now().plusDays(1));
        Mockito.when(userRepository.findUserByUsername(ArgumentMatchers.anyString())).
                thenReturn(Optional.of(new User()));
        Mockito.when(lotRepository.save(ArgumentMatchers.any()))
                .thenReturn(new Lot());


        Assertions.assertNotNull(lotService.createLot(lotRequest, new UserPrincipal("user")));
    }

    @Test
    public void getLotById_LotIsNull_ShouldThrowException(){
        Mockito.when(lotRepository.findByIdAndArchivalIsFalse(ArgumentMatchers.any()))
                .thenReturn(Optional.empty());

        Throwable throwable = Assertions.assertThrows(LotNotFoundException.class,
                () -> lotService.getLotById((long) 1));
        Assertions.assertNotNull(throwable);
    }

    @Test
    public void getLotById_LotIsNotNull_ShouldReturnLot(){
        Mockito.when(lotRepository.findByIdAndArchivalIsFalse(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(new Lot()));

        Assertions.assertNotNull(lotService.getLotById((long) 1));
    }

    @Test
    public void deleteLot_LotIsNull_ShouldThrowException() {
        Mockito.when(userRepository.findUserByUsername(ArgumentMatchers.anyString())).
                thenReturn(Optional.of(new User()));
        Mockito.when(lotRepository.findByIdAndArchivalIsFalse(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.empty());

        Throwable throwable = Assertions.assertThrows(LotNotFoundException.class,
                () -> lotService.deleteLot((long) 1, new UserPrincipal("user")));
        Assertions.assertNotNull(throwable);
    }

    @Test
    public void deleteLot_LotIsNotActive_ShouldThrowException() {
        Mockito.when(userRepository.findUserByUsername(ArgumentMatchers.anyString())).
                thenReturn(Optional.of(new User()));
        Lot lot = new Lot("", "", LocalDateTime.now(), LocalDateTime.now().plusMinutes(10),
                BigDecimal.ZERO, false, false, new ArrayList<>());
        Mockito.when(lotRepository.findByIdAndArchivalIsFalse(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(lot));

        Throwable throwable = Assertions.assertThrows(LotDeleteException.class,
                () -> lotService.deleteLot((long) 1, new UserPrincipal("user")));
        Assertions.assertNotNull(throwable);
    }

    @Test
    public void deleteLot_UserHasNotAccess_ShouldThrowException() {
        User currUser = new User();
        currUser.setId((long) 10);
        currUser.setEmail("");
        currUser.setPassword("");
        currUser.setRoles(new HashSet<>());

        Mockito.when(userRepository.findUserByUsername(ArgumentMatchers.anyString())).
                thenReturn(Optional.of(currUser));

        Lot lot = new Lot("", "", LocalDateTime.now(), LocalDateTime.now().plusMinutes(10),
                BigDecimal.ZERO, true, false, new ArrayList<>());
        lot.setUser(new User());

        Mockito.when(lotRepository.findByIdAndArchivalIsFalse(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(lot));


        Throwable throwable = Assertions.assertThrows(AccessDeniedException.class,
                () -> lotService.deleteLot((long) 1, new UserPrincipal("user")));
        Assertions.assertNotNull(throwable);
    }

    @Test
    public void deleteLot_UserHasAccess_ShouldPerformDeletion() {
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
        lot.setUser(currUser);

        Mockito.when(lotRepository.findByIdAndArchivalIsFalse(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(lot));


        lotService.deleteLot((long) 1, new UserPrincipal("user"));

        lot.setArchival(true);
        Mockito.verify(lotRepository, Mockito.times(1)).save(lot);
    }


}

package com.craft.BiddingSystemTest.jobs;

import com.craft.biddingSystem.jobs.LotCompletionCheckJob;
import com.craft.biddingSystem.models.Bet;
import com.craft.biddingSystem.models.Lot;
import com.craft.biddingSystem.services.LotService;
import com.craft.biddingSystem.services.WinningBetService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@SpringBootTest
public class LotCompletionCheckJobTest {

    @Autowired
    private LotCompletionCheckJob checkJob;

    @MockBean
    LotService lotService;
    @MockBean
    WinningBetService winningBetService;
    @MockBean
    SchedulerFactoryBean schedulerFactoryBean;

    @Test
    public void execute_NoActiveLots_ShouldBePerformed() throws JobExecutionException {
        Mockito.when(lotService.getAllLots(true)).thenReturn(new ArrayList<>());

        checkJob.execute(null);

        Mockito.verify(lotService, Mockito.times(1)).saveLots(new ArrayList<>());
        Mockito.verify(winningBetService, Mockito.times(1)).saveWinningBets(new ArrayList<>());
    }

    @Test
    public void execute_LotsWithoutCompleteTime_ShouldBePerformed() throws JobExecutionException {
        List<Lot> lots = new ArrayList<>();
        lots.add(new Lot("", "", LocalDateTime.now(), LocalDateTime.now().plusMinutes(10),
                BigDecimal.ZERO, true, false, new ArrayList<>()));


        Mockito.when(lotService.getAllLots(true)).thenReturn(lots);

        checkJob.execute(null);

        Mockito.verify(lotService, Mockito.times(1)).saveLots(new ArrayList<>());
        Mockito.verify(winningBetService, Mockito.times(1)).saveWinningBets(new ArrayList<>());
    }

    @Test
    public void execute_LotsWithoutMaxBet_ShouldBePerformed() throws JobExecutionException {
        Lot lot = new Lot("", "", LocalDateTime.now(), LocalDateTime.now().minusMinutes(10),
                BigDecimal.ZERO, true, false, new ArrayList<>());
        List<Lot> lots = new ArrayList<>();
        lots.add(lot);


        Mockito.when(lotService.getAllLots(true)).thenReturn(lots);

        checkJob.execute(null);

        List<Lot> expectedLots = new ArrayList<>();
        lot.setActive(false);
        expectedLots.add(lot);

        Mockito.verify(lotService, Mockito.times(1)).saveLots(expectedLots);
        Mockito.verify(winningBetService, Mockito.times(1)).saveWinningBets(new ArrayList<>());
    }

    @Test
    public void execute_LotsWithMaxBet_ShouldBePerformed() throws JobExecutionException {
        Lot lot = new Lot("", "", LocalDateTime.now(), LocalDateTime.now().minusMinutes(10),
                BigDecimal.ZERO, true, false, new ArrayList<>());
        List<Bet> bets = new ArrayList<>();
        bets.add(new Bet((long)1, lot, (long)1, BigDecimal.ONE, LocalDateTime.now(), false));
        lot.setBets(bets);
        List<Lot> lots = new ArrayList<>();
        lots.add(lot);


        Mockito.when(lotService.getAllLots(true)).thenReturn(lots);

        checkJob.execute(null);

        List<Lot> expectedLots = new ArrayList<>();
        lot.setActive(false);
        expectedLots.add(lot);

        Mockito.verify(lotService, Mockito.times(1)).saveLots(expectedLots);
        Mockito.verify(winningBetService, Mockito.times(1)).saveWinningBets(ArgumentMatchers.anyList());
    }
}

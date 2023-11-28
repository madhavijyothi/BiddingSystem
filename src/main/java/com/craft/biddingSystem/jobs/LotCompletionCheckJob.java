package com.craft.biddingSystem.jobs;

import com.craft.biddingSystem.models.Bet;
import com.craft.biddingSystem.models.Lot;
import com.craft.biddingSystem.models.WinningBet;
import com.craft.biddingSystem.services.LotService;
import com.craft.biddingSystem.services.WinningBetService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

// Job that checks completion of lots
@Component
public class LotCompletionCheckJob implements Job {
    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    LotService lotService;
    @Autowired
    WinningBetService winningBetService;

    public LotCompletionCheckJob() {
    }


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        List<Lot> activeLots = lotService.getAllLots(true);

        LocalDateTime currTime = LocalDateTime.now();

        List<Lot> modifiedLots = new ArrayList<>();
        List<WinningBet> winningBets = new ArrayList<>();

        for (Lot lot : activeLots) {
            // Check if lot is complete
            if(currTime.isAfter(lot.getEndDate())){
                logger.info("Lot with id {} was changed", lot.getId());
                lot.setActive(false);
                modifiedLots.add(lot);

                // Find winning bet
                Optional<Bet> maxBet = lot.getBets().stream()
                        .filter(bet -> !bet.isArchival())
                        .max(Comparator.comparing(Bet::getAmount));

                if(maxBet.isPresent()){
                    WinningBet winningBet = new WinningBet();
                    winningBet.setBet(maxBet.get());
                    winningBet.setCreatedDate(currTime);

                    winningBets.add(winningBet);
                }
            }
        }

        lotService.saveLots(modifiedLots);
        winningBetService.saveWinningBets(winningBets);

        logger.info("Check Job is performed");
    }
}

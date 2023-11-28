package com.craft.biddingSystem.repository;

import com.craft.biddingSystem.models.Bet;
import com.craft.biddingSystem.models.Lot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BetRepository extends JpaRepository<Bet, Long> {

    Optional<Bet> findByIdAndArchivalIsFalse(Long id);

    List<Bet> findAllByLotAndArchivalIsFalse(Lot lot);

}

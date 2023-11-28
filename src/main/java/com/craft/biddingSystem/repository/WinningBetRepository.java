package com.craft.biddingSystem.repository;

import com.craft.biddingSystem.models.WinningBet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WinningBetRepository extends JpaRepository<WinningBet, Long> {

    List<WinningBet> findAllByBetUserId(long id);
}

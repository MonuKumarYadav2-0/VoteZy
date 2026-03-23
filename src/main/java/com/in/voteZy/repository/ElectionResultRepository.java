package com.in.voteZy.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.in.voteZy.entity.ElectionResult;

public interface ElectionResultRepository extends JpaRepository<ElectionResult,Long>{
  Optional<ElectionResult> findByElectionName(String electionName);
  
  
  @Modifying(clearAutomatically = true, flushAutomatically = true)
  @Query(value = "UPDATE election_result SET winner_id = NULL WHERE winner_id IS NOT NULL", nativeQuery = true)
  void resetWinner();
}

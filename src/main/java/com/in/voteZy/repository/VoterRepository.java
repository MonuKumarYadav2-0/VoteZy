package com.in.voteZy.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.in.voteZy.entity.Voter;

public interface VoterRepository extends JpaRepository<Voter,Long> {
  boolean existsByEmail(String email);
}

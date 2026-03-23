package com.in.voteZy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.in.voteZy.entity.Candidate;

public interface CandidateRepository extends JpaRepository<Candidate,Long>{
 List<Candidate> findAllByOrderByVoteCountDesc();
}

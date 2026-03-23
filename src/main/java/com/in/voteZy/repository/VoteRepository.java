package com.in.voteZy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.in.voteZy.entity.Vote;

@Repository
public interface VoteRepository extends JpaRepository<Vote,Long> {
	@Modifying
    @Query(value = "TRUNCATE TABLE vote", nativeQuery = true)
    void truncateVotes();
	
	@Modifying
	@Query(value = "DELETE FROM vote WHERE candidate_id = :candidateId", nativeQuery = true)
	void deleteByCandidateId(@Param("candidateId") Long candidateId);
}

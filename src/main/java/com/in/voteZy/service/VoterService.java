package com.in.voteZy.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.in.voteZy.entity.Candidate;
import com.in.voteZy.entity.Vote;
import com.in.voteZy.entity.Voter;
import com.in.voteZy.exception.DuplicateResourceException;
import com.in.voteZy.exception.ResourceNotFoundException;
import com.in.voteZy.repository.CandidateRepository;
import com.in.voteZy.repository.VoteRepository;
import com.in.voteZy.repository.VoterRepository;

import jakarta.transaction.Transactional;

@Service
public class VoterService {
	private VoterRepository voterRepository;
	private CandidateRepository candidateRepository;
	private VoteRepository voteRepository;

	@Autowired
	public VoterService(VoterRepository voterRepository, CandidateRepository candidateRepository,VoteRepository voteRepository) {

		this.voterRepository = voterRepository;
		this.candidateRepository = candidateRepository;
		this.voteRepository =voteRepository;
	}

	public Voter registerVoter(Voter voter) {
		if (voterRepository.existsByEmail(voter.getEmail())) {
			throw new DuplicateResourceException("Voter with email id " + voter.getEmail() + " all ready exist");
		}
		voter.setHasVoted(false);
		return voterRepository.save(voter);

	}

	public List<Voter> getAllVoter() {
		return voterRepository.findAll();
	}

	public Voter getVoterById(Long id) {
		Voter voter = voterRepository.findById(id).orElse(null);
		if (voter == null) {
			throw new ResourceNotFoundException("Voter with id " + id + " Not Found");
		}
		return voter;
	}

	public Voter updateVoter(Long id, Voter updatedVoter) {
		Voter voter = voterRepository.findById(id).orElse(null);
		if (voter == null) {
			throw new ResourceNotFoundException("Voter with id " + id + " Not Found");
		}

		if (updatedVoter.getName() != null) {
			voter.setName(updatedVoter.getName());
		}

		if (updatedVoter.getEmail() != null) {
			voter.setEmail(updatedVoter.getEmail());
		}

		return voterRepository.save(voter);
	}

	@Transactional
	public void deleteVoter(Long id) {
	    Voter voter = voterRepository.findById(id).orElse(null);
	    if (voter == null) {
	        throw new ResourceNotFoundException("Voter with id:" + id + " not found");
	    }

	    // Agar voter ne vote diya tha
	    Vote vote = voter.getVote();
	    if (vote != null) {
	        // Step 1 — Candidate ka voteCount - 1 karo
	        Candidate candidate = vote.getCandidate();
	        if (candidate != null) {
	            candidate.setVoteCount(
	                candidate.getVoteCount() == null ? 0 : candidate.getVoteCount() - 1
	            );
	            candidateRepository.save(candidate);
	        }

	        // Step 2 — Voter ka vote reference null karo
	        voter.setVote(null);
	        voterRepository.save(voter);
	        voterRepository.flush();

	        // Step 3 — Vote table se bhi delete karo ← YEH FIX HAI
	        voteRepository.delete(vote);
	        voteRepository.flush();
	    }

	    // Step 4 — Voter delete karo
	    voterRepository.delete(voter);
	}
}

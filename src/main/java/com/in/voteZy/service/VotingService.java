package com.in.voteZy.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.in.voteZy.entity.Candidate;
import com.in.voteZy.entity.Vote;
import com.in.voteZy.entity.Voter;
import com.in.voteZy.exception.ResourceNotFoundException;
import com.in.voteZy.exception.VoteNotAllowedException;
import com.in.voteZy.repository.CandidateRepository;
import com.in.voteZy.repository.VoteRepository;
import com.in.voteZy.repository.VoterRepository;
import jakarta.transaction.Transactional;

@Service
public class VotingService {

    private VoteRepository voteRepository;
    private CandidateRepository candidateRepository;
    private VoterRepository voterRepository;

    public VotingService(VoteRepository voteRepository,
                         CandidateRepository candidateRepository,
                         VoterRepository voterRepository) {
        this.voteRepository = voteRepository;
        this.candidateRepository = candidateRepository;
        this.voterRepository = voterRepository;
    }

    @Transactional
    public Vote castVote(Long voterId, Long candidateId) {

        if (!voterRepository.existsById(voterId)) {
            throw new ResourceNotFoundException("Voter not found with id " + voterId);
        }
        if (!candidateRepository.existsById(candidateId)) {
            throw new ResourceNotFoundException("Candidate not found with id " + candidateId);
        }

        Voter voter = voterRepository.findById(voterId).get();

        // ← FIX 1: Boolean.TRUE.equals() — NullPointerException nahi aayega
        if (Boolean.TRUE.equals(voter.getHasVoted())) {
            throw new VoteNotAllowedException("Voter with id " + voterId + " has already casted his vote");
        }

        Candidate candidate = candidateRepository.findById(candidateId).get();

        Vote vote = new Vote();
        vote.setVoter(voter);
        vote.setCandidate(candidate);

        // ← FIX 2: null check — NullPointerException nahi aayega
        candidate.setVoteCount((candidate.getVoteCount() == null ? 0 : candidate.getVoteCount()) + 1);

        voter.setHasVoted(true);
        voter.setVote(vote);
        voterRepository.save(voter);

        return vote;
    }

    public List<Vote> getAllVote() {
        return voteRepository.findAll();
    }
}
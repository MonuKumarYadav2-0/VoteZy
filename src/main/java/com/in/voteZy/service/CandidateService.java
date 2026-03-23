package com.in.voteZy.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.in.voteZy.entity.Candidate;
import com.in.voteZy.entity.ElectionResult;
import com.in.voteZy.entity.Vote;
import com.in.voteZy.entity.Voter;
import com.in.voteZy.exception.ResourceNotFoundException;
import com.in.voteZy.repository.CandidateRepository;
import com.in.voteZy.repository.ElectionResultRepository;
import com.in.voteZy.repository.VoteRepository;
import com.in.voteZy.repository.VoterRepository;

@Service
public class CandidateService {

    private CandidateRepository candidateRepository;
    private VoterRepository voterRepository;
    private ElectionResultRepository electionResultRepository;
    private VoteRepository voteRepository;

    @Autowired
    public CandidateService(CandidateRepository candidateRepository,
                            VoterRepository voterRepository,
                            ElectionResultRepository electionResultRepository,
                            VoteRepository voteRepository) {
        this.candidateRepository = candidateRepository;
        this.voterRepository = voterRepository;
        this.electionResultRepository = electionResultRepository;
        this.voteRepository = voteRepository;
    }

    public Candidate addCandidate(Candidate candidate) {
        return candidateRepository.save(candidate);
    }

    public List<Candidate> getAllCandidates() {
        return candidateRepository.findAll();
    }

    public Candidate getCandidateById(Long id) {
        Candidate candidate = candidateRepository.findById(id).orElse(null);
        if (candidate == null) {
            throw new ResourceNotFoundException("Candidate with id:" + id + " not found");
        }
        return candidate;
    }

    public Candidate updateCandidate(Long id, Candidate updatedCandidate) {
        Candidate candidate = getCandidateById(id);
        if (updatedCandidate.getName() != null) {
            candidate.setName(updatedCandidate.getName());
        }
        if (updatedCandidate.getParty() != null) {
            candidate.setParty(updatedCandidate.getParty());
        }
        return candidateRepository.save(candidate);
    }

    @Transactional
    public void deleteCandidate(Long id) {
        Candidate candidate = getCandidateById(id);

        // Step 1 — ElectionResult winner wali lines HATAO — winner field nahi hai ab
        // (winnerName, winnerParty, winnerVotes already saved hain — safe hain) ✅

        // Step 2 — Voters ka hasVoted false karo + vote reference null karo
        List<Vote> votes = candidate.getVote();
        for (Vote v : votes) {
            Voter voter = v.getVoter();
            if (voter != null) {
                voter.setHasVoted(false);
                voter.setVote(null);
                voterRepository.save(voter);
            }
        }
        voterRepository.flush();

        // Step 3 — Native query se votes delete karo
        voteRepository.deleteByCandidateId(id);
        voteRepository.flush();

        // Step 4 — Candidate ki list clear karo
        candidate.getVote().clear();

        // Step 5 — Candidate delete karo
        candidateRepository.delete(candidate);
    }
}
     
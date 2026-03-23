package com.in.voteZy.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.in.voteZy.entity.Voter;
import com.in.voteZy.repository.CandidateRepository;
import com.in.voteZy.repository.ElectionResultRepository;
import com.in.voteZy.repository.VoteRepository;
import com.in.voteZy.repository.VoterRepository;
import jakarta.persistence.EntityManager;

@Service
public class ResetElectionService {

    private final VoteRepository voteRepository;
    private final VoterRepository voterRepository;
    private final CandidateRepository candidateRepository;
    private final ElectionResultRepository electionResultRepository;
    private final EntityManager entityManager;

    public ResetElectionService(VoteRepository voteRepository,
                                VoterRepository voterRepository,
                                CandidateRepository candidateRepository,
                                ElectionResultRepository electionResultRepository,
                                EntityManager entityManager) {
        this.voteRepository = voteRepository;
        this.voterRepository = voterRepository;
        this.candidateRepository = candidateRepository;
        this.electionResultRepository = electionResultRepository;
        this.entityManager = entityManager;
    }

    @Transactional
    public String resetElection() {

        // Step 1 — Voter ka vote reference NULL + hasVoted false
        List<Voter> voters = voterRepository.findAll();
        voters.forEach(v -> {
            v.setVote(null);
            v.setHasVoted(false);
        });
        voterRepository.saveAll(voters);
        voterRepository.flush();

        // Step 2 — Votes TRUNCATE
        voteRepository.truncateVotes();

        // Step 3 — Cache clear
        entityManager.clear();

        // Step 4 — ElectionResult winner null wali lines HATA DI
        // winner field nahi hai ab — winnerName/Party/Votes safe hain ✅

        // Step 5 — Candidates delete
        candidateRepository.deleteAll();

        return "Election reset successfully! Previous results are preserved.";
    }
}
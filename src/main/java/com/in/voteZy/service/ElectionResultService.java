package com.in.voteZy.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.in.voteZy.entity.Candidate;
import com.in.voteZy.entity.ElectionResult;
import com.in.voteZy.exception.ResourceNotFoundException;
import com.in.voteZy.repository.CandidateRepository;
import com.in.voteZy.repository.ElectionResultRepository;
import com.in.voteZy.repository.VoteRepository;

@Service
public class ElectionResultService {

    private ElectionResultRepository electionResultRepository;
    private CandidateRepository candidateRepository;
    private VoteRepository voteRepository;

    public ElectionResultService(ElectionResultRepository electionResultRepository,
                                  CandidateRepository candidateRepository,
                                  VoteRepository voteRepository) {
        this.electionResultRepository = electionResultRepository;
        this.candidateRepository = candidateRepository;
        this.voteRepository = voteRepository;
    }

    @Transactional
    public ElectionResult declareElectionResult(String electionName) {
        Optional<ElectionResult> existingResult = electionResultRepository
                .findByElectionName(electionName);
        if (existingResult.isPresent()) {
            return existingResult.get();
        }
        if (voteRepository.count() == 0) {
            throw new IllegalStateException(
                    "Can not declare the result as no vote has been casted");
        }
        List<Candidate> candidateList = candidateRepository
                .findAllByOrderByVoteCountDesc();
        if (candidateList.isEmpty()) {
            throw new ResourceNotFoundException("Candidate not found");
        }

        Candidate winner = candidateList.get(0);
        int totalVote = candidateList.stream()
                .mapToInt(c -> c.getVoteCount() == null ? 0 : c.getVoteCount())
                .sum();

        // ← Saare candidates ka snapshot banao
        StringBuilder snapshot = new StringBuilder("[");
        for (int i = 0; i < candidateList.size(); i++) {
            Candidate c = candidateList.get(i);
            snapshot.append("{")
                    .append("\"name\":\"").append(c.getName()).append("\",")
                    .append("\"party\":\"").append(c.getParty()).append("\",")
                    .append("\"votes\":").append(c.getVoteCount() == null ? 0 : c.getVoteCount())
                    .append("}");
            if (i < candidateList.size() - 1) snapshot.append(",");
        }
        snapshot.append("]");

        ElectionResult result = new ElectionResult();
        result.setElectionName(electionName);
        result.setTotalVotes(totalVote);
        result.setWinnerName(winner.getName());
        result.setWinnerParty(winner.getParty());
        result.setWinnerVotes(winner.getVoteCount() == null ? 0 : winner.getVoteCount());
        result.setCandidatesSnapshot(snapshot.toString()); // ← Save karo

        return electionResultRepository.save(result);
    }
    public List<ElectionResult> getAllREsult() {
        return electionResultRepository.findAll();
    }
    
    public void deleteResult(Long id) {
        ElectionResult result = electionResultRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Result with id:" + id + " not found"));
        electionResultRepository.delete(result);
    }

    public void deleteAllResults() {
        electionResultRepository.deleteAll();
    }
}
package com.in.voteZy.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.in.voteZy.DTO.VoteRequestDTO;
import com.in.voteZy.DTO.VoteResponseDTO;
import com.in.voteZy.entity.Vote;
import com.in.voteZy.service.ResetElectionService;
import com.in.voteZy.service.VotingService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/vote")
@CrossOrigin
public class VotingController {
private VotingService votingService;
private ResetElectionService resetElectionService;

public VotingController(VotingService votingService, ResetElectionService resetElectionService) {
	
	this.votingService = votingService;
	this.resetElectionService=resetElectionService;
}

@PostMapping("/cast")
public ResponseEntity<VoteResponseDTO> castVote(@RequestBody @Valid VoteRequestDTO voteRequest)
{
	Vote vote=votingService.castVote(voteRequest.getVoterId(),voteRequest.getCandidateId());
	VoteResponseDTO voteResponse=new VoteResponseDTO("Vote casted successfully",true,vote.getVoterId(),vote.getCandidateId());
	return new ResponseEntity<>(voteResponse,HttpStatus.CREATED);
}

@GetMapping("/getallvotes")
public ResponseEntity<List<Vote>> getAllVotes()
{
	List<Vote> voteList=votingService.getAllVote();
	return new ResponseEntity<>(voteList,HttpStatus.OK);
}

@DeleteMapping("/reset")
public ResponseEntity<String> resetElection() {
    String msg = resetElectionService.resetElection();
    return ResponseEntity.ok(msg);
}

}

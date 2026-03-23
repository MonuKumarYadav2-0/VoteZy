package com.in.voteZy.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.in.voteZy.DTO.ElectionREsultResponseDTO;
import com.in.voteZy.DTO.ElectionResultRequestDTO;
import com.in.voteZy.entity.ElectionResult;
import com.in.voteZy.service.ElectionResultService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/election-result")
@CrossOrigin
public class ElectionResultController {
private ElectionResultService electionResultService;

@Autowired
public ElectionResultController(ElectionResultService electionResultService) {
	
	this.electionResultService = electionResultService;
}

@PostMapping("/declare")
public ResponseEntity<ElectionREsultResponseDTO> declareElectionResult(
        @RequestBody @Valid ElectionResultRequestDTO electionResultRequest) {
    
    ElectionResult result = electionResultService
            .declareElectionResult(electionResultRequest.getElectionName());
    
    ElectionREsultResponseDTO responseDTO = new ElectionREsultResponseDTO();
    responseDTO.setName(result.getElectionName());
    responseDTO.setTotalVotes(result.getTotalVotes());
    responseDTO.setWinnerVotes(result.getWinnerVotes()); // ← directly result se
    
    return ResponseEntity.ok(responseDTO);
}

@GetMapping("/getallresults")
public ResponseEntity<List<ElectionResult>> getAllResult()
{
	List<ElectionResult> results=electionResultService.getAllREsult();
	return ResponseEntity.ok(results);
}

//Single result delete
@DeleteMapping("/delete/{id}")
public ResponseEntity<String> deleteResult(@PathVariable Long id) {
 electionResultService.deleteResult(id);
 return ResponseEntity.ok("Result with id:" + id + " deleted successfully");
}

//Saare results delete
@DeleteMapping("/deleteall")
public ResponseEntity<String> deleteAllResults() {
 electionResultService.deleteAllResults();
 return ResponseEntity.ok("All results deleted successfully");
}

}

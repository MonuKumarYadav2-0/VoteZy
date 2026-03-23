package com.in.voteZy.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.in.voteZy.entity.Candidate;
import com.in.voteZy.service.CandidateService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/candidate")
@CrossOrigin
public class CandidateController {
private CandidateService candidateService;
@Autowired
public CandidateController(CandidateService candidateService) {
	this.candidateService = candidateService;
}
@PostMapping("/add")
public ResponseEntity<Candidate>addCandidate(@Valid @RequestBody Candidate candidate )
{
	Candidate savedCandidate=candidateService.addCandidate(candidate);
	return new ResponseEntity<>(savedCandidate,HttpStatus.CREATED);
}

@GetMapping("/getallcandidate")
public ResponseEntity<List<Candidate>>getAllCandidate( )
{
	List<Candidate>candidateList=candidateService.getAllCandidates();
	return new ResponseEntity<>(candidateList,HttpStatus.OK);
}

@GetMapping("/getcandidate/{id}")
public ResponseEntity<Candidate>getCandidate(@PathVariable Long id)
{
	Candidate candidate=candidateService.getCandidateById(id);
	return new ResponseEntity<>(candidate,HttpStatus.OK);
}

@PutMapping("/updatecandidate/{id}")
public ResponseEntity<Candidate>updateCandidate(@PathVariable Long id ,@RequestBody Candidate candidate)
{
	Candidate updatedCandidate=candidateService.updateCandidate(id,candidate);
	return new ResponseEntity<>(updatedCandidate,HttpStatus.OK);
}

@DeleteMapping("/delete/{id}")
public ResponseEntity<String>deleteCandidate(@PathVariable Long id)
{
	candidateService.deleteCandidate(id);
	return new ResponseEntity<>("Candidate with id:"+id+" deleted successfully",HttpStatus.OK);
}


}

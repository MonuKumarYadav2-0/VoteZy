package com.in.voteZy.entity;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
//@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Vote {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@OneToOne
	@JoinColumn(name = "voter_id")
	@JsonIgnore
	private Voter voter;
	@ManyToOne
	@JoinColumn(name = "candidate_id")
	@JsonIgnore
	@OnDelete(action = OnDeleteAction.CASCADE) 
	private Candidate candidate;

	@JsonProperty("candidateId")
	public Long getCandidateId() {
		return candidate != null ? candidate.getId() : null;
	}

	@JsonProperty("votereId")
	public Long getVoterId() {
		return voter != null ? voter.getId() : null;
	}
}

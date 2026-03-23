package com.in.voteZy.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ElectionResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Election name is required")
    private String electionName;

    private int totalVotes;
    private String winnerName;
    private String winnerParty;
    private int winnerVotes;

    @Column(columnDefinition = "TEXT")
    private String candidatesSnapshot;

    // winner field aur getWinnerId() BILKUL NAHI ← sab hatao
}
package com.in.voteZy.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class VoteRequestDTO {
	@NotNull(message="voter id is required")
private Long voterId;
	@NotNull(message="voter id is required")
private Long candidateId;
}

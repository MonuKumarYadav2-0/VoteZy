package com.in.voteZy.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ElectionResultRequestDTO {
	@NotBlank(message="Election name is reqired")
private String electionName;
}

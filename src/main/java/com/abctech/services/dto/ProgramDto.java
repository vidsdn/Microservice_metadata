package com.abctech.services.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProgramDto {

	private Long programId;

	@NotEmpty(message = "Program Name is mandatory")
	private String programName;

	@NotEmpty(message = "Program Description is mandatory")
	private String programDescription;

	@NotEmpty(message = "Program Status is mandatory")
	private String programStatus;
}
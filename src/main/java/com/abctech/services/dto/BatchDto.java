package com.abctech.services.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BatchDto {
	private Integer batchId;

	@NotBlank(message = "Batch Name is mandatory")
	private String batchName;

	private String batchDescription;

	@NotBlank(message = "Batch status is mandatory")
	private String batchStatus;

	@Positive(message = " No of Classes is needed; It should be a positive number ")
	private int batchNoOfClasses;

	@NotNull(message = " ProgramId field is mandatory.  ")
	private Long programId;

	private String programName;

}

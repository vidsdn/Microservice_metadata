package com.abctech.services.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.abctech.services.config.ApiResponse;
import com.abctech.services.dto.ProgramDto;
import com.abctech.services.exception.DuplicateResourceFoundException;
import com.abctech.services.exception.ResourceNotFoundException;
import com.abctech.services.service.ProgramService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

@RestController
@RequestMapping("/api")
public class ProgramController {

	@Autowired
	private ProgramService programService;

	@GetMapping(value = "/program")
	private ResponseEntity<List<ProgramDto>> getPrograms() {
		List<ProgramDto> programList = programService.getAllPrograms();
		return ResponseEntity.ok(programList);
	}

	@GetMapping(path = "program/{programId}")
	private ResponseEntity<ProgramDto> getProgramById(@PathVariable("programId") Long programId)
			throws ResourceNotFoundException {
		return ResponseEntity.ok().body(programService.getProgramById(programId));
	}

	@PostMapping(path = "/program")
	private ResponseEntity<ProgramDto> createAndSaveProgram(@Valid @RequestBody ProgramDto programDto)
			throws DuplicateResourceFoundException {
		ProgramDto newProgramDto = programService.createAndSaveProgram(programDto);
		return ResponseEntity.status(HttpStatus.CREATED).body(newProgramDto);
	}

	@PutMapping(path = "/program/{programId}")
	private ResponseEntity<ProgramDto> updateProgramById(@PathVariable("programId") @NotBlank @Positive Long programId,
			@Valid @RequestBody ProgramDto modifyProgram) throws ResourceNotFoundException {
		return ResponseEntity.ok(programService.updateProgramById(programId, modifyProgram));
	}

	@DeleteMapping(path = "/program/{programId}")
	private ResponseEntity<ApiResponse> deleteByProgramId(@PathVariable("programId") @NotBlank @Positive Long programId)
			throws ResourceNotFoundException {
		programService.deleteByProgramId(programId);
		return new ResponseEntity<ApiResponse>(new ApiResponse("Program deleted successfully", true), HttpStatus.OK);
	}

}

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
import com.abctech.services.dto.ClassDto;
import com.abctech.services.exception.DuplicateResourceFoundException;
import com.abctech.services.exception.ResourceNotFoundException;
import com.abctech.services.service.ClassService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@RestController
@RequestMapping("/api")
public class ClassController {

	@Autowired
	ClassService classService;

	// createClass
	@PostMapping(path = "/class")
	private ResponseEntity<ClassDto> createAndSaveClass(@Valid @RequestBody ClassDto classDTO)
			throws DuplicateResourceFoundException {
		ClassDto savedClassDTO = classService.createClass(classDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body(savedClassDTO);
	}

	// GetAllClasses
	@GetMapping(value = "/class")
	private ResponseEntity<?> getAllClassesList() throws ResourceNotFoundException {

		List<ClassDto> ClassesList = classService.getAllClasses();
		return ResponseEntity.ok(ClassesList);
	}

	// GetClassesByClassId
	@GetMapping(path = "class/{classId}")
	private ResponseEntity<?> getClassesById(@PathVariable("classId") @NotBlank @Positive Long classId)
			throws ResourceNotFoundException {
		ClassDto classesDTOList = classService.getClassByClassId(classId);
		return ResponseEntity.ok(classesDTOList);
	}

	@GetMapping(path = "classesbyBatch/{batchId}", produces = "application/json")
	private ResponseEntity<?> getClassesByBatchId(@PathVariable("batchId") @NotBlank @Positive Integer batchId)
			throws ResourceNotFoundException {
		System.out.println("in get All classes by BatchId");
		List<ClassDto> classesDTOList = classService.getClassesByBatchId(batchId);
		return ResponseEntity.ok(classesDTOList);
	}

	// UpdateClassByClassId
	@PutMapping(path = "class/{classId}")
	private ResponseEntity<ClassDto> updateClassScheduleById(@PathVariable @NotBlank @Positive Long classId,
			@Valid @RequestBody ClassDto updateClassSchedule) throws ResourceNotFoundException {
		return ResponseEntity.ok(classService.updateClassByClassId(classId, updateClassSchedule));
	}

	// DeleteClassByClassId
	@DeleteMapping(path = "class/{classId}", produces = "application/json")
	private ResponseEntity<?> deleteByClassId(@PathVariable("classId") @NotBlank @Positive Long classId)
			throws ResourceNotFoundException {
		classService.deleteByClassId(classId);
		return new ResponseEntity<ApiResponse>(new ApiResponse("Class deleted successfully " + classId, true),
				HttpStatus.OK);
	}

	// Promote an asset - Updating Status of all child assets based on Parent
	// ProgramId
	@PutMapping(path = "/promote/{programId}/status/{status}")
	public String updateClassBatchProgramStatus(@PathVariable(value = "programId") @Positive Long programId,
			@PathVariable(value = "status") @NotNull String status) throws ResourceNotFoundException {
		classService.updateClassBatchProgramStatus(programId, status);
		return "Updated Status Successfully";
	}

}

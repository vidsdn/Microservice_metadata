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
import com.abctech.services.dto.BatchDto;
import com.abctech.services.service.BatchService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;


@RestController
@RequestMapping("/api")
public class BatchController  {

    @Autowired
    private BatchService batchService;
    
    @GetMapping("/batch")
    public ResponseEntity<List<BatchDto>> getAllBatches() {
		List<BatchDto> batchList = batchService.getAllBatches();;
		return ResponseEntity.ok(batchList);
    }
    
	@GetMapping ( path = "/batch/{batchId}")
	public ResponseEntity<BatchDto> getBatchById(@PathVariable(value="batchId") @Positive Integer batchId) {
		return ResponseEntity.ok(batchService.findBatchById(batchId) );
	}
	
	
	//Get Batch List by Program
	@GetMapping ( path = "/batch/program/{programId}", produces = "application/json")    
	public ResponseEntity<List<BatchDto>> getBatchByProgram(@PathVariable(value="programId") @Positive Long programId) {
		return ResponseEntity.ok(batchService.findBatchByProgramId(programId));
	}
	
	
    @PostMapping(path = "/batch")
    public ResponseEntity<BatchDto> createBatch( @Valid @RequestBody BatchDto batchDto ) {
        BatchDto newBatchDto = batchService.createBatch(batchDto );
        return ResponseEntity.status(HttpStatus.CREATED).body(newBatchDto);
    }
    

    @PutMapping(path = "/batch/{batchId}")
    public ResponseEntity<BatchDto> updateBatch( @Valid @RequestBody BatchDto batchDto,  @PathVariable Integer batchId ) {
    	BatchDto updatedBatch = batchService.updateBatch( batchDto, batchId);
    	return ResponseEntity.ok(updatedBatch );
    }

    
    @DeleteMapping(path = "/batch/{bacthId}" )
    public ResponseEntity<ApiResponse> deleteBatch( @PathVariable Integer bacthId) {
        batchService.deleteBatch(bacthId);
        return new ResponseEntity<ApiResponse>(new ApiResponse("Batch deleted successfully " + bacthId , true), HttpStatus.OK);

    }
    

	
    
}

package com.abctech.services.service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import com.abctech.services.dto.BatchDto;
import com.abctech.services.entity.Batch;
import com.abctech.services.entity.Class;
import com.abctech.services.entity.Program;
import com.abctech.services.exception.DuplicateResourceFoundException;
import com.abctech.services.exception.ResourceNotFoundException;
import com.abctech.services.mapper.BatchMapper;
import com.abctech.services.repository.BatchRepository;
import com.abctech.services.repository.ProgramRepository;

@Service
public class BatchService {
	@Autowired
	private BatchRepository batchRepository;

	@Autowired
	private ProgramRepository programRepository;

	@Autowired
	private BatchMapper batchMapper;

	public List<BatchDto> getAllBatches() {
		return batchMapper.toBatchDtos(batchRepository.findAll());
	}

	public BatchDto findBatchById(Integer batchId) {
		Batch batch = batchRepository.findById(batchId)
				.orElseThrow(() -> new ResourceNotFoundException("Batch not found for the given batch Id " + batchId));
		return batchMapper.toBatchDto(batch);
	}

	// get Batches by Program ID
	public List<BatchDto> findBatchByProgramId(Long programid) {
		List<Batch> result = batchRepository.findByProgramProgramId(programid);
		if (!(result.size() <= 0)) {
			return (batchMapper.toBatchDtos(result));

		} else {
			throw new ResourceNotFoundException("batch with this programId " + programid + "not found");
		}
	}

	// create new Batch under a Program
	public BatchDto createBatch(BatchDto batchDto) {
		Long programId = batchDto.getProgramId();
		Batch newBatch = batchMapper.toBatch(batchDto);
		Program program = programRepository.findById(programId)
				.orElseThrow(() -> new ResourceNotFoundException("Program not found with the ProgramID " + programId));

		newBatch.setProgram(program);

		Batch result = batchRepository.findByBatchNameAndProgram_ProgramId(newBatch.getBatchName(), programId);
		if (result != null)
			throw new DuplicateResourceFoundException("Program " + program.getProgramName() + " with Batch-"
					+ newBatch.getBatchName() + " already exists:"
					+ "  Please give a different batch Name or Choose a different Program");

		newBatch.setCreationTime(new Timestamp(new Date().getTime()));
		newBatch.setLastModTime(new Timestamp(new Date().getTime()));

		Batch createdBatch = batchRepository.save(newBatch);
		return batchMapper.toBatchDto(createdBatch);
	}

	public BatchDto updateBatch(BatchDto batchDto, Integer batchId) {
		Batch existingBatch = batchRepository.findById(batchId)
				.orElseThrow(() -> new ResourceNotFoundException("Batch not found for the given batch Id " + batchId));

		Batch updateBatch = batchMapper.toBatch(batchDto);
		Long programId = batchDto.getProgramId();
		Program program = programRepository.findById(programId)
				.orElseThrow(() -> new ResourceNotFoundException("Program not found with the ProgramID " + programId));
		updateBatch.setProgram(program);

		updateBatch.setCreationTime(existingBatch.getCreationTime());
		updateBatch.setLastModTime(new Timestamp(new Date().getTime()));
		updateBatch.setBatchId(existingBatch.getBatchId()); // setting existing batch id
		return batchMapper.toBatchDto(batchRepository.save(updateBatch));
	}

	public void deleteBatch(Integer batchId) {
		batchRepository.findById(batchId)
				.orElseThrow(() -> new ResourceNotFoundException("Batch not found for the given batch Id " + batchId));
		batchRepository.deleteById(batchId);
	}

}

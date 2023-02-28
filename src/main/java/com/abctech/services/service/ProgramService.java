package com.abctech.services.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.abctech.services.dto.ProgramDto;
import com.abctech.services.entity.Program;
import com.abctech.services.exception.DuplicateResourceFoundException;
import com.abctech.services.exception.ResourceNotFoundException;
import com.abctech.services.mapper.ProgramMapper;
import com.abctech.services.repository.ProgramRepository;


@Service
public class ProgramService {

	@Autowired
	private ProgramRepository programRepository;

	@Autowired
	private ProgramMapper programMapper;

	public List<ProgramDto> getAllPrograms() {
		List<Program> programList = programRepository.findAll();
		return (programMapper.toProgramDtoList(programList));
	}

	public ProgramDto getProgramById(Long programId) {

		Program program = programRepository.findById(programId).orElseThrow(
				() -> new ResourceNotFoundException("program not found for the given programId " + programId));

		return programMapper.toProgramDto(program);

	}

	public ProgramDto createAndSaveProgram(ProgramDto programDto) {

		Program newProgram = programMapper.toProgram(programDto);

		LocalDateTime now = LocalDateTime.now();
		Timestamp timestamp = Timestamp.valueOf(now);

		newProgram.setCreationTime(timestamp);
		newProgram.setLastModTime(timestamp);

		Optional<Program> result = programRepository.findByProgramName(newProgram.getProgramName());

		if (result.isPresent()) {
			throw new DuplicateResourceFoundException("cannot create program , since it already exists");
		} else {

			Program savedProgram = programRepository.save(newProgram);
			ProgramDto savedProgramDto = programMapper.toProgramDto(savedProgram);
			return (savedProgramDto);
		}

	}

	public ProgramDto updateProgramById(Long programId, ProgramDto programDto)throws ResourceNotFoundException
	{
		LocalDateTime now= LocalDateTime.now();
		Timestamp timestamp= Timestamp.valueOf(now);

		Program savedProgram = programRepository.findById(programId)
					.orElseThrow(() -> new ResourceNotFoundException("program not found for the given programId " + programId));
			
			Program updateProgram = programMapper.toProgram(programDto);
			updateProgram.setProgramId(programId);		
			updateProgram.setProgramName(programDto.getProgramName());
			updateProgram.setProgramDescription(programDto.getProgramDescription());
			updateProgram.setProgramStatus(programDto.getProgramStatus());
			updateProgram.setCreationTime(savedProgram.getCreationTime());
			updateProgram.setLastModTime(timestamp);

			Program updatedProgram = programRepository.save(updateProgram);
			ProgramDto updatedProgramDto =programMapper.toProgramDto(updatedProgram);
			return updatedProgramDto;
	}

	public void deleteByProgramId(Long programId)throws ResourceNotFoundException
	{
		Program program = programRepository.findById(programId)
				.orElseThrow(() -> new ResourceNotFoundException("program not found for the given programId " + programId));
		programRepository.deleteById(programId);

	}

}

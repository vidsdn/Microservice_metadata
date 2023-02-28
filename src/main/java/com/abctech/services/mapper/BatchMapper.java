package com.abctech.services.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.abctech.services.dto.BatchDto;
import com.abctech.services.entity.Batch;

@Mapper(componentModel = "spring", uses = ProgramMapper.class)
public interface BatchMapper {
	BatchMapper INSTANCE = Mappers.getMapper(BatchMapper.class);

	@Mapping(source = "batch.program.programId", target = "programId")
	@Mapping(source = "batch.program.programName", target = "programName")
	BatchDto toBatchDto(Batch batch);

	@Mapping(source = "dto.programId", target = "program.programId")
	Batch toBatch(BatchDto dto);

	List<BatchDto> toBatchDtos(List<Batch> batches);
}

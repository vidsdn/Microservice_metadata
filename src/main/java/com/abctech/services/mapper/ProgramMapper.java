package com.abctech.services.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.abctech.services.dto.ProgramDto;
import com.abctech.services.entity.Program;

@Mapper(componentModel = "spring")
public interface ProgramMapper {
	ProgramMapper INSTANCE = Mappers.getMapper(ProgramMapper.class);

	ProgramDto toProgramDto(Program program);

	Program toProgram(ProgramDto programDto);

	List<ProgramDto> toProgramDtoList(List<Program> programEntities);

	List<Program> toProgramList(List<ProgramDto> ProgramDtos);

}

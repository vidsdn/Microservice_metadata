package com.abctech.services.mapper;

import java.util.List;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.abctech.services.dto.ClassDto;
import com.abctech.services.entity.Class;

@Mapper(componentModel = "spring", uses = { BatchMapper.class })
public interface ClassMapper {
	ClassMapper INSTANCE = Mappers.getMapper(ClassMapper.class);

	@Mapping(source = "batchInClass.batchId", target = "batchId")
	ClassDto toClassSchdDTO(Class savedEntity);

	@InheritInverseConfiguration
	Class toClassScheduleEntity(ClassDto classSchdDTO);

	List<ClassDto> toClassScheduleDTOList(List<Class> classSchdEntites);

	List<Class> toClassScheduleEntityList(List<ClassDto> classSchdDTOs);

}
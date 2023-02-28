package com.abctech.services.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.abctech.services.entity.Batch;
import com.abctech.services.entity.Class;
import com.abctech.services.entity.Program;
import com.abctech.services.exception.DuplicateResourceFoundException;
import com.abctech.services.exception.ResourceNotFoundException;
import com.abctech.services.mapper.ClassMapper;
import com.abctech.services.dto.ClassDto;
import com.abctech.services.repository.BatchRepository;
import com.abctech.services.repository.ClassRepository;

import lombok.SneakyThrows;

@ExtendWith(MockitoExtension.class)
public class ClassServiceTest {

	@Mock
	private ClassRepository classRepository;

	@Mock
	private BatchRepository batchRepository;

	@InjectMocks
	private ClassService classService;

	@Mock
	private ClassMapper classMapper;

	private Class mockClass, mockClass2;

	private ClassDto mockClassDto, mockClassDto2;

	private List<Class> classList;

	private List<ClassDto> classDtoList;

	@BeforeEach
	public void setup() {
		mockClass = setMockClassAndDto();
	}

	private Class setMockClassAndDto() {
		String sDate = "02/28/2023";
		Date classDate = null;
		try {
			classDate = new SimpleDateFormat("dd/mm/yyyy").parse(sDate);
		} catch (ParseException e) {

			e.printStackTrace();
		}
		LocalDateTime now = LocalDateTime.now();
		Timestamp timestamp = Timestamp.valueOf(now);

		Program program = new Program((long) 7, "Django", "new Prog", "nonActive", timestamp, timestamp);
		Batch batchInClass = new Batch(3, "SDET 1", "SDET Batch 1", "Active", program, 5, timestamp, timestamp);

		mockClass = new Class((long) 1, batchInClass, 1, classDate, "Selenium", "Active", "10", "SeleniumClass",
				"comments", "c:/ClassNotes", "c:/RecordingPath", timestamp, timestamp);

		mockClassDto = new ClassDto(1L, 1, 1, classDate, "Selenium1", "Active", "10", "Selenium Class", "comments",
				"c:/ClassNotes", "c:/RecordingPath");

		mockClass2 = new Class((long) 2, batchInClass, 2, classDate, "Selenium1", "Active", "10", "Selenium Class1",
				"OK", "c:/ClassNotes", "c:/RecordingPath", timestamp, timestamp);

		mockClassDto2 = new ClassDto((long) 2, 3, 2, classDate, "Selenium1", "Active", "10", "Selenium Class1",
				"comments2", "c:/ClassNotes", "c:/RecordingPath");

		classList = new ArrayList<>();
		classDtoList = new ArrayList<>();
		return mockClass;

	}

	private Batch setMockBatch() {
		LocalDateTime now = LocalDateTime.now();
		Timestamp timestamp = Timestamp.valueOf(now);
		Program program = new Program((long) 7, "Django", "new Prog", "nonActive", timestamp, timestamp);
		Batch batch = new Batch(3, "SDET 1", "SDET Batch 1", "Active", program, 5, timestamp, timestamp);

		return batch;
	}

	@Nested
	class GetSClass {

		@DisplayName("test - Get AlL Class ")
		@SneakyThrows
		@Test
		public void testGetAllClass() throws ResourceNotFoundException {

			classList.add(mockClass);
			classList.add(mockClass2);

			classDtoList.add(mockClassDto);
			classDtoList.add(mockClassDto2);
			when(classRepository.findAll()).thenReturn(classList);
            when(classMapper.toClassScheduleDTOList(classList)).thenReturn(classDtoList);


			List<ClassDto> classDtos = classService.getAllClasses();

			assertThat(classDtos.size()).isGreaterThan(0);

			verify(classRepository).findAll();
			verify(classMapper).toClassScheduleDTOList(classList);

		}

		@DisplayName("test - Get All ClassSchedule  When List is Empty")
		@SneakyThrows
		@Test
		public void testGetAllClassScheduleWhenListIsEmpty() throws ResourceNotFoundException {

			given(classRepository.findAll()).willReturn(Collections.emptyList());

			assertThrows(ResourceNotFoundException.class, () -> classService.getAllClasses());

			Mockito.verify(classRepository).findAll();

		}

		@DisplayName("test - get Class By Class Id")
		@SneakyThrows
		@Test
		public void testGetClassByClassId() throws ResourceNotFoundException {

			given(classRepository.findById(mockClass.getCsId())).willReturn(Optional.of(mockClass));
            given(classMapper.toClassSchdDTO(mockClass)).willReturn(mockClassDto);

			ClassDto classDto = classService.getClassByClassId(mockClass.getCsId());

			assertThat(classDto).isNotNull();

		}

		@DisplayName("test for creating a new class")
		@SneakyThrows
		@Test
		void testCreateClass() throws DuplicateResourceFoundException {

			Batch batch = setMockBatch();

			when(batchRepository.findById(mockClass.getBatchInClass().getBatchId())).thenReturn(Optional.of(batch));
	        when(classMapper.toClassScheduleEntity(mockClassDto)).thenReturn(mockClass);
			when(classRepository.save(mockClass)).thenReturn(mockClass);
			when(classMapper.toClassSchdDTO(mockClass)).thenReturn(mockClassDto);

			ClassDto classDto = classService.createClass(mockClassDto);

			AssertionsForClassTypes.assertThat(classDto).isNotNull();

		}

		@DisplayName("test for deleting class when id is not found")
		@SneakyThrows
		@Test
		public void testDeleteClassIdNotFound() {

			Long classId = 50L;
			when(classRepository.existsById(classId)).thenReturn(false);

			assertThrows(ResourceNotFoundException.class, () -> classService.deleteByClassId(classId));

			Mockito.verify(classRepository).existsById(classId);

		}

		@DisplayName("test -get class by batchId")
		@SneakyThrows
		@Test
		void testGetClassesForBatch() {

			Integer batchId = 1;

			classList.add(mockClass);
			classList.add(mockClass2);
			classDtoList.add(mockClassDto);
			classDtoList.add(mockClassDto2);

			given(classRepository.findByBatchInClass_batchId(batchId)).willReturn(classList);
			given(classMapper.toClassScheduleDTOList(classList)).willReturn(classDtoList);
			
			List<ClassDto> classDtos = classService.getClassesByBatchId(batchId);

			assertThat(classDtos).isNotNull();
			assertThat(classDtos.size()).isGreaterThan(0);
		}

		@DisplayName("test - to update class by class Id")
		@SneakyThrows
		@Test
		void testUpdateClassByClassId() {
			given(classRepository.findById(mockClassDto.getCsId())).willReturn(Optional.of(mockClass));
			mockClassDto.setClassComments("new class");

			given(batchRepository.findById(mockClassDto.getBatchId()))
					.willReturn(Optional.of(mockClass.getBatchInClass()));
			given(classRepository.findByCsIdAndBatchInClass_BatchId(mockClassDto.getCsId(), mockClassDto.getBatchId()))
					.willReturn(Optional.of(mockClass));
			given(classRepository.save(mockClass)).willReturn(mockClass);
			given(classMapper.toClassSchdDTO(mockClass)).willReturn(mockClassDto);
			
			ClassDto classDto = classService.updateClassByClassId(mockClassDto.getCsId(), mockClassDto);

			assertThat(classDto).isNotNull();
			assertThat(classDto.getClassComments()).isEqualTo("new class");

		}

	}
}

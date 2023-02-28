package com.abctech.services.controller;

import static org.mockito.BDDMockito.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.abctech.services.dto.ClassDto;
import com.abctech.services.exception.ResourceNotFoundException;
import com.abctech.services.service.ClassService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.SneakyThrows;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(ClassController.class)
public class ClassControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ClassService classService;

	@Autowired
	private ObjectMapper objectMapper;

	private ClassDto mockClassDto;

	private List<ClassDto> classDtoList;

	@BeforeEach
	public void setup() {

		setMockClassAndDto();

	}

	private void setMockClassAndDto() {
		String sDate = "28/02/2023";
		Date classDate = null;
		try {
			classDate = new SimpleDateFormat("dd/mm/yyyy").parse(sDate);
		} catch (ParseException e) {

			e.printStackTrace();
		}

		mockClassDto = new ClassDto(1L, 1, 1, classDate, "Selenium1", "Active", "10", "Selenium Class", "comments",
				"c:/ClassNotes", "c:/RecordingPath");
		classDtoList = new ArrayList<ClassDto>();
	}

	@Nested
	class GetOperation {
		@DisplayName("test - to Get All Classes")
		@SneakyThrows
		@Test
		void testGetAllClass() {
			
			ClassDto mockClassDto2 = mockClassDto;
			mockClassDto2.setCsId(2L);
			mockClassDto2.setClassTopic("Selenium Test");
			classDtoList.add(mockClassDto);
			classDtoList.add(mockClassDto2);

			given(classService.getAllClasses()).willReturn(classDtoList);
	
			ResultActions response = mockMvc.perform(get("/class"));
		
			response.andExpect(status().isOk()).andDo(print()).andExpect(jsonPath("$", hasSize(classDtoList.size())));

			verify(classService).getAllClasses();
		}

		@DisplayName("test - Get Classes By ClassId")
		@SneakyThrows
		@Test
		void testGetClassById() {
			
			Long classId = 1L;
			System.out.println("mockClassDto" + mockClassDto);
			given(classService.getClassByClassId(classId)).willReturn(mockClassDto);

			ResultActions response = mockMvc.perform(get("/class/{classId}", classId));

			response.andExpect(status().isOk()).andDo(print())
					.andExpect(jsonPath("$.classTopic", is(mockClassDto.getClassTopic())));

			verify(classService).getClassByClassId(classId);

		}

		@DisplayName("test -Class By ClassId not found")
		@SneakyThrows
		@Test
		void testGetClassByIdNotFound() {

			Long classId = 10L;
			String message = "Class with Class ID  not found";
			classDtoList.add(mockClassDto);
			when(classService.getClassByClassId(ArgumentMatchers.any(Long.class)))
					.thenThrow(new ResourceNotFoundException(message));

			ResultActions response = mockMvc.perform(get("/class/{classId}", classId));

			response.andExpectAll(status().isNotFound(), jsonPath("$.message").value(message)).andDo(print());

			verify(classService).getClassByClassId(ArgumentMatchers.any(Long.class));

		}

		@DisplayName("test - Get all Class  by batchId ")
		@SneakyThrows
		@Test
		void testGetClassByBatchId() {

			Integer batchId = 1;
			ClassDto mockClassDto2 = mockClassDto;
			mockClassDto2.setCsId(4L);
			mockClassDto2.setClassTopic("PostgreSql Test");
			classDtoList.add(mockClassDto);
			classDtoList.add(mockClassDto2);

			given(classService.getClassesByBatchId(batchId)).willReturn(classDtoList);

			ResultActions response = mockMvc.perform(get("/classesbyBatch/{batchId}", batchId));

			response.andExpectAll(status().isOk()).andDo(print())
					.andExpect(jsonPath("$", hasSize(classDtoList.size())));
			verify(classService).getClassesByBatchId(batchId);

		}

		@DisplayName("test -Classes By BatchId not found")
		@SneakyThrows
		@Test
		void testGetClassByBatchIdNotFound() {

			Integer batchId = 3;
			String message = "Class with Batch ID 3 not found";
			classDtoList.add(mockClassDto);
			when(classService.getClassesByBatchId(ArgumentMatchers.any(Integer.class)))
					.thenThrow(new ResourceNotFoundException(message));

			ResultActions response = mockMvc.perform(get("/classesbyBatch/{batchId}", batchId));

			response.andExpectAll(status().isNotFound(), jsonPath("$.message").value(message)).andDo(print());

		}

	}

	@DisplayName("test - to create a new Class")
	@SneakyThrows
	@Test
	void testCreateClass() {

		given(classService.createClass(ArgumentMatchers.any(ClassDto.class))).willAnswer((i) -> i.getArgument(0));

		ResultActions response = mockMvc.perform(post("/class").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(mockClassDto)));

		response.andExpect(status().isCreated()).andExpect(jsonPath("$.csId", is(mockClassDto.getCsId()), Long.class))
				.andExpect(jsonPath("$.classTopic", is(mockClassDto.getClassTopic())));

		verify(classService).createClass(ArgumentMatchers.any(ClassDto.class));
	}

	@Nested
	class PutOperation {
		@DisplayName("test - to Update Class Schedule by Id")
		@SneakyThrows
		@Test
		void testUpdateClassByClassId() {
			Long classId = 1L;
			ClassDto updatedClassDto = mockClassDto;
			updatedClassDto.setClassTopic("New Selenium Class");
			when(classService.updateClassByClassId(ArgumentMatchers.any(Long.class),
					ArgumentMatchers.any(ClassDto.class))).thenReturn(updatedClassDto);

			ResultActions response = mockMvc.perform(put("/class/{classId}", classId)
					.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(updatedClassDto)));
			System.out.println(response);

			response.andExpectAll(status().isOk(), content().contentType(MediaType.APPLICATION_JSON),
					jsonPath("$.classTopic", is(updatedClassDto.getClassTopic()))).andDo(print());

			verify(classService).updateClassByClassId(ArgumentMatchers.any(Long.class),
					ArgumentMatchers.any(ClassDto.class));

		}

		@DisplayName("test - Update class by Id  Not Found")
		@SneakyThrows
		@Test
		public void testUpdateClassByIdNotFound() {

			Long classId = 7L;
			String message = "Class with class id  not found";
			ClassDto updatedClassDto = mockClassDto;
			updatedClassDto.setClassTopic("New Selenium Class");
			when(classService.updateClassByClassId(ArgumentMatchers.any(Long.class),
					ArgumentMatchers.any(ClassDto.class))).thenThrow(new ResourceNotFoundException(message));

			ResultActions response = mockMvc.perform(put("/class/{classId}", classId)
					.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(updatedClassDto)));

			response.andExpectAll(status().isNotFound(), jsonPath("$.message").value(message)).andDo(print());

			verify(classService).updateClassByClassId(ArgumentMatchers.any(Long.class),
					ArgumentMatchers.any(ClassDto.class));
		}

	}
	
	@DisplayName("test for deleting an class")
	@Test
	void testDeleteClass() throws Exception {

		Long classId = 2L;
		willDoNothing().given(classService).deleteByClassId(classId);

		ResultActions response = mockMvc.perform(delete("/class/{classId}", classId));

		response.andExpect(status().isOk())
				.andDo(print());
	}
}

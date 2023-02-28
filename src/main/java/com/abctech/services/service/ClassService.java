package com.abctech.services.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.abctech.services.dto.ClassDto;
import com.abctech.services.entity.Class;
import com.abctech.services.entity.Program;
import com.abctech.services.event.AssetPromotionEvent;
import com.abctech.services.entity.Batch;
import com.abctech.services.exception.DuplicateResourceFoundException;
import com.abctech.services.exception.ResourceNotFoundException;
import com.abctech.services.kafka.AssetPromotionProducer;
import com.abctech.services.mapper.ClassMapper;
import com.abctech.services.repository.BatchRepository;
import com.abctech.services.repository.ClassRepository;
import com.abctech.services.repository.ProgramRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ClassService {
	@Autowired
	private ClassRepository classRepository;

	@Autowired
	BatchRepository batchRepository;

	@Autowired
	ProgramRepository programRepository;

	@Autowired
	private ClassMapper classMapper;
	
	@Autowired
	private AssetPromotionProducer assetPromotionProducer;

	public ClassDto createClass(ClassDto newClassDto) throws DuplicateResourceFoundException {

		int batchId = newClassDto.getBatchId();

		Batch batchobj = batchRepository.findById(batchId)
				.orElseThrow(() -> new ResourceNotFoundException("Batch", "Id", batchId));

		Class newClass = classMapper.toClassScheduleEntity(newClassDto);

		LocalDateTime now = LocalDateTime.now();
		Timestamp timestamp = Timestamp.valueOf(now);

		newClass.setCreationTime(timestamp);
		newClass.setLastModTime(timestamp);

		Class savedClass = classRepository.save(newClass);
		return classMapper.toClassSchdDTO(savedClass);

	}

	public List<ClassDto> getAllClasses() throws ResourceNotFoundException {
		List<Class> ClassScheduleList = classRepository.findAll();
		return (classMapper.toClassScheduleDTOList(ClassScheduleList));
	}

	// get class by classId
	public ClassDto getClassByClassId(Long id) throws ResourceNotFoundException {
		Class ClassScheduleById = classRepository.findById(id).get();
		if (ClassScheduleById == null) {
			throw new ResourceNotFoundException("ClassSchedule is not found for classId :" + id);
		} else {
			return (classMapper.toClassSchdDTO(ClassScheduleById));
		}
	}

	// get all classes by batchId
	@Transactional
	public List<ClassDto> getClassesByBatchId(Integer batchId)
			throws ResourceNotFoundException, IllegalArgumentException {

		List<Class> result = classRepository.findByBatchInClass_batchId(batchId);
		if (!(result.size() <= 0)) {
			return (classMapper.toClassScheduleDTOList(result));

		} else {
			throw new ResourceNotFoundException("classes with the batchId " + batchId + " not found");
		}

	}

	// Update Class Schedules by Id
	public ClassDto updateClassByClassId(Long id, ClassDto updateClassSchedule) throws ResourceNotFoundException {

		Integer batchid = updateClassSchedule.getBatchId();

		Class savedClass = this.classRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Class", "Id", id));

		Batch batchobj = batchRepository.findById(batchid)
				.orElseThrow(() -> new ResourceNotFoundException("Batch", "Id", batchid));

		Class classBatchOptional = classRepository.findByCsIdAndBatchInClass_BatchId(id, batchid)
				.orElseThrow(() -> new ResourceNotFoundException(
						"ClassId with " + id + " and batchId with " + batchid + " not found"));

		LocalDateTime now = LocalDateTime.now();
		Timestamp timestamp = Timestamp.valueOf(now);

		Class toUpdateClass = classMapper.toClassScheduleEntity(updateClassSchedule);

		toUpdateClass.setCreationTime(savedClass.getCreationTime());

		if (updateClassSchedule.getBatchId().getClass() == Integer.class)
			toUpdateClass.setBatchInClass(batchobj);
		toUpdateClass.setLastModTime(timestamp);

		Class updatedClass = this.classRepository.save(toUpdateClass);
		ClassDto updatedClassDto = classMapper.toClassSchdDTO(updatedClass);
		return updatedClassDto;
	}

	// delete by classId
	public void deleteByClassId(Long classId) throws ResourceNotFoundException {
		classRepository.findById(classId)
				.orElseThrow(() -> new ResourceNotFoundException("Class not found for the given class Id " + classId));
		classRepository.deleteById(classId);

	}

	@Transactional
	public void updateClassBatchProgramStatus(Long programId, String status) throws ResourceNotFoundException {

		Program program = programRepository.findById(programId).orElseThrow(
				() -> new ResourceNotFoundException("program not found for the given programId " + programId));

		programRepository.updateProgramStatus(status, programId);
		log.info("program status updated for program {} " , programId);

		List<Batch> batchesByProgram = batchRepository.findByProgramProgramId(programId);

		System.out.println(batchesByProgram);
		int totalNoOfBatches = batchesByProgram.size();
		if (totalNoOfBatches > 0) {
			for (int noOfBatches = 0; noOfBatches < totalNoOfBatches; noOfBatches++) {
				Batch batch = batchesByProgram.get(noOfBatches);
				Integer batchId = batch.getBatchId();
				//System.out.println("batch inside for loop" + batch);
				// update batch
				batchRepository.updateBatchStatus(status, batchId);
				log.info("batch status updated for batch {} " , batchId);
				List<Class> classesByBatch = classRepository.findByBatchInClass_batchId(batch.getBatchId());
				int totalNoOfClasses = classesByBatch.size();
				if (totalNoOfClasses > 0) {
					for (int noOfClasses = 0; noOfClasses < totalNoOfClasses; noOfClasses++) {
						Class class1 = classesByBatch.get(noOfClasses);
						Long classID = class1.getCsId();
						//System.out.println("class inside for loop" + class1);
						// update class
						classRepository.updateClassStatus(status, classID);
						log.info("class status updated for class {} " , classID);
					}
				}

			}
		}
		log.info("updated Event Status for programId {} ", programId);
        
        AssetPromotionEvent assetPromotionEvent = new AssetPromotionEvent();
        assetPromotionEvent.setStatus(status);
        assetPromotionEvent.setMessage("Updated status for the Assets");
        assetPromotionEvent.setProgramId(programId);
        log.info("Sending Asset Promotion Event for programId {} ", programId);
        assetPromotionProducer.sendMessage(assetPromotionEvent);
        		

		
	}
}

package com.abctech.services.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.abctech.services.entity.Batch;

@Transactional
@Repository
public interface BatchRepository extends JpaRepository<Batch, Integer> {

	List<Batch> findByProgramProgramId(Long programId);

	Batch findByBatchNameAndProgram_ProgramId(String batchName, Long programId);

	@Modifying
	@Query(value = "update tbl_batch  set batch_status = ? , last_mod_time= CURRENT_TIMESTAMP  where batch_id =  ?", nativeQuery = true)
	void updateBatchStatus(String status, Integer batchId);
}

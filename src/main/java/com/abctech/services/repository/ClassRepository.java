package com.abctech.services.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.abctech.services.entity.Class;

@Transactional
@Repository
public interface ClassRepository extends JpaRepository<Class, Long> {

	@Query(value = "SELECT * FROM tbl_class_schedule WHERE batch_id = ?1", nativeQuery = true)
	List<Class> findByBatchInClass_batchId(Integer batchId);

	Optional<Class> findByCsIdAndBatchInClass_BatchId(Long csId, Integer batchid);

	@Modifying
	@Query(value = "update tbl_class_schedule  set class_status = ? , last_mod_time= CURRENT_TIMESTAMP  where cs_id =  ?", nativeQuery = true)
	void updateClassStatus(String status, Long classId);

}

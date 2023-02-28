package com.abctech.services.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.abctech.services.entity.Program;

@Transactional
@Repository
public interface ProgramRepository extends JpaRepository<Program, Long> {

	Optional<Program> findByProgramName(String programName);

	@Modifying
	@Query(value = "update tbl_program  set program_status = ? , last_mod_time= CURRENT_TIMESTAMP  where program_id =  ?", nativeQuery = true)
	void updateProgramStatus(String status, Long programId);
}

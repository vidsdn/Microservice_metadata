package com.abctech.services.entity;

import java.sql.Timestamp;

import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tbl_program")
public class Program {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "program_id_generator")
	@SequenceGenerator(name = "program_id_generator", sequenceName = "tbl_program_program_id_seq", allocationSize = 1)
	@Column(name = "program_id")
	private Long programId;

	@Column(name = "program_name")
	private String programName;

	@Column(name = "program_description")
	private String programDescription;

	@Column(name = "program_status")
	private String programStatus;

	@Column(name = "creation_time")
	private Timestamp creationTime;

	@Column(name = "last_mod_time")
	private Timestamp lastModTime;

}

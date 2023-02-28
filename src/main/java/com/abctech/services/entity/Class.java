package com.abctech.services.entity;

import java.sql.Timestamp;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Table(name = "tbl_class_schedule")
public class Class {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "class_id_generator")
	@SequenceGenerator(name = "class_id_generator", sequenceName = "tbl_class_sch_cs_id_seq", allocationSize = 1)
	@Column(name = "cs_id")
	private Long csId;

	@ManyToOne
	@JoinColumn(name = "batch_id", nullable = false)
	@jakarta.persistence.Embedded
	@AttributeOverride(name = "batchId", column = @Column(name = "batch_id"))
	private Batch batchInClass;

	@Column(name = "class_no")
	private Integer classNo;

	@DateTimeFormat(pattern = "dd/MM/yyyy h:mm a")
	@Column(name = "class_date")
	private Date classDate;

	@Column(name = "class_topic")
	private String classTopic;

	@Column(name = "class_status")
	private String classStatus;

	@Column(name = "class_no_of_students")
	private String classNoOfStudents;

	@Column(name = "class_description")
	private String classDescription;

	@Column(name = "class_comments")
	private String classComments;

	@Column(name = "class_notes")
	private String classNotes;

	@Column(name = "class_recording_path")
	private String classRecordingPath;

	@Column(name = "creation_time")
	private Timestamp creationTime;

	@Column(name = "last_mod_time")
	private Timestamp lastModTime;
}

package com.abctech.services.entity;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "tbl_batch")
public class Batch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="batch_id")
    private Integer batchId;
    
    @Column(name="batch_name")
    private String batchName;
   
    @Column(name="batch_description")
    private String batchDescription;
    
    @Column(name="batch_status")
    private String batchStatus;

	@ManyToOne ( fetch = FetchType.LAZY)        
    @JoinColumn ( name = "batch_program_id", nullable = false )                            
    private Program program;                         

	@Column(name="batch_no_of_classes")
	private Integer batchNoOfClasses;
     
	@Column(name="creation_time")
	private Timestamp creationTime;

	@Column(name="last_mod_time")
	private Timestamp lastModTime;
}
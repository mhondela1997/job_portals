package com.makanza.job_portals.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import com.makanza.job_portals.model.Job;

import jakarta.persistence.LockModeType;

public interface JobRepository extends JpaRepository<Job,Long>{
	  @Lock(LockModeType.PESSIMISTIC_WRITE) // Locks row for update
	    @Query("SELECT j FROM Job j WHERE j.id = :jobId")
	    Job findByIdForUpdate(Long jobId);
}

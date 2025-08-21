package com.makanza.job_portals.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.makanza.job_portals.model.Application;
import com.makanza.job_portals.model.Job;
import com.makanza.job_portals.model.MyUser;

public interface ApplicationRepository extends JpaRepository<Application, Long>{
List<Application> findByJobId(Long jobId);
boolean existsByJobAndEmail(Job job, String email);

// get all applications for a user
List<Application> findByEmail(String email);

@Query("SELECT COUNT(DISTINCT a.user.id) FROM Application a")
long countDistinctApplicants();

@Query("SELECT a.user FROM Application a WHERE a.job.id = :jobId")
List<MyUser> findApplicantsByJobId(@Param("jobId") Long jobId);
}

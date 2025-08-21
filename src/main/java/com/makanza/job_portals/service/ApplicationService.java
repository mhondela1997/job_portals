package com.makanza.job_portals.service;





import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.makanza.job_portals.model.Application;
import com.makanza.job_portals.model.Job;
import com.makanza.job_portals.model.MyUser;
import com.makanza.job_portals.repository.ApplicationRepository;
import com.makanza.job_portals.repository.JobRepository;
import com.makanza.job_portals.repository.UserRepository;

import org.springframework.transaction.annotation.Transactional;


@Service
public class ApplicationService {

    @Autowired
    private ApplicationRepository applicationRepository;
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JobRepository jobRepository;
    @Transactional
    public void applyForJob(Long jobId, Application application, String username) {
        Job job = jobRepository.findByIdForUpdate(jobId);
        if (job == null) {
            throw new RuntimeException("Job not found");
        }

        if (!"OPEN".equals(job.getStatus()) || job.getVacancies() <= 0) {
            throw new RuntimeException("Job is not available for application");
        }

        MyUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        application.setJob(job);
        application.setUser(user);   // 🔴 this was missing!
        application.setUsername(username);
        application.setCandidateName(user.getUsername());
        application.setEmail(user.getEmail());

        // placeholder scoring
        application.setResumeScore(80);
        application.setLocationPoints(10);
        application.setFinalScore(90);

        applicationRepository.save(application); // should now persist ✅

        job.setVacancies(job.getVacancies() - 1);
        if (job.getVacancies() == 0) {
            job.setStatus("CLOSED");
        }
        jobRepository.save(job);
    }

}
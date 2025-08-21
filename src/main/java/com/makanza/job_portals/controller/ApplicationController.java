package com.makanza.job_portals.controller;

import com.makanza.job_portals.model.Application;
import com.makanza.job_portals.model.Job;
import com.makanza.job_portals.repository.ApplicationRepository;
import com.makanza.job_portals.repository.JobRepository;
import com.makanza.job_portals.service.ApplicationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/applications")
public class ApplicationController {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private JobRepository jobRepository; 

    @Autowired
    private ApplicationService applicationService;

    // Show application form for a job
    @GetMapping("/apply/{jobId}")
    public String showApplyForm(@PathVariable Long jobId, Model model) {
        Job job = jobRepository.findById(jobId).orElseThrow();
        model.addAttribute("job", job);
        model.addAttribute("application", new Application());
        return "applications/apply"; // form page
    }

    @PostMapping("/apply/{jobId}")
    public String applyForJob(@PathVariable Long jobId,
                              @ModelAttribute Application application,
                              Model model) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        try {
            applicationService.applyForJob(jobId, application, username);
            model.addAttribute("success", "Application submitted successfully!");
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
        }

        return "redirect:/applications/summary";
    }


    // View summary of all applications
    @GetMapping("/summary")
    public String viewSummary(Model model) {
        List<Application> apps = applicationRepository.findAll();
        model.addAttribute("applications", apps);
        return "applications/summary"; // summary page
    }
    @GetMapping("/apply")
    public String apply(Model model) {
    	List<Job> jobs = jobRepository.findAll();
        model.addAttribute("jobs", jobs);
    	return "applications/apply1";
    }
    
    
    
    
}

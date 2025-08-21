
package com.makanza.job_portals.controller;

import com.makanza.job_portals.model.Job;
import com.makanza.job_portals.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/jobs")
public class JobController {

    @Autowired
    private JobRepository jobRepository;

    // View all jobs
    @GetMapping
    public String listJobs(Model model) {
        List<Job> jobs = jobRepository.findAll();
        model.addAttribute("jobs", jobs);
        return "admin/jobs";  
    }

    // Show job creation form
    @GetMapping("/new")
    public String showJobForm(Model model) {
        model.addAttribute("job", new Job());
        return "admin/job-form";  
        }

    // Handle job creation
    @PostMapping
    public String createJob(@ModelAttribute Job job) {
    	 job.setStatus("OPEN");
        jobRepository.save(job);
        return "redirect:/admin/jobs";
    }
    
    //close job manually
    @GetMapping("/close/{id}")
    public String closeJob(@PathVariable Long id) {
    	jobRepository.findById(id).ifPresent(job->{
    		job.setStatus("CLOSED");
    		jobRepository.save(job);
    	});
    	return "redirect:/admin/jobs";
    }
 // Reopen job (only if vacancies > 0)
    @GetMapping("/open/{id}")
    public String openJob(@PathVariable Long id, Model model) {
        jobRepository.findById(id).ifPresent(job -> {
            if (job.getVacancies() > 0) {  //check vacancies
                job.setStatus("OPEN");
                jobRepository.save(job);
            } else {
                // Optional: send error message
                model.addAttribute("error", "Job cannot be reopened. No vacancies left.");
            }
        });
        return "redirect:/admin/jobs";
    }

}

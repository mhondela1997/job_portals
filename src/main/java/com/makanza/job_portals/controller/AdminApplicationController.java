package com.makanza.job_portals.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.makanza.job_portals.model.Application;
import com.makanza.job_portals.model.Job;
import com.makanza.job_portals.model.MyUser;
import com.makanza.job_portals.repository.ApplicationRepository;
import com.makanza.job_portals.repository.JobRepository;
import com.makanza.job_portals.service.NotificationService;

@Controller
@RequestMapping("/admin/applications")
public class AdminApplicationController {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private JobRepository jobRepository;
    
    @Autowired
    private NotificationService notificationService;

    @Autowired
    private JavaMailSender mailSender;

    // Select a job to view applicants
    @GetMapping("/select-job")
    public String selectJobForApplicants(Model model) {
        List<Job> jobs = jobRepository.findAll();
        model.addAttribute("jobs", jobs);
        return "admin/select-job";
    }

    // View all applications for a specific job
    @GetMapping("/jobs/{jobId}")
    public String viewApplicationForJob(@PathVariable Long jobId, Model model) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        List<Application> applications = applicationRepository.findByJobId(jobId);
        model.addAttribute("job", job);
        model.addAttribute("applications", applications);

        return "admin/applications";
    }

    @PostMapping("/{id}/approve")
    public String approveApplication(@PathVariable("id") Long applicantId,
                                     RedirectAttributes redirectAttributes) {
        try {
            // Approve the application
            Application app = applicationRepository.findById(applicantId)
                               .orElseThrow(() -> new RuntimeException("Application not found"));
            app.setStatus("APPROVED");
            applicationRepository.save(app);

            notificationService.createNotification("Your application for '" + app.getJob().getTitle() + "' has been approved.", app.getUser());
            
            // Send notification email
            try {
                notifyApplicant(app, "Congratulations! Your application has been APPROVED.");
                redirectAttributes.addFlashAttribute("successMessage", "Application approved and email sent.");
            } catch (Exception e) {
                e.printStackTrace();
                redirectAttributes.addFlashAttribute("warningMessage", "Application approved, but failed to send email.");
            }

            return "redirect:/admin/applications/jobs/" + app.getJob().getId();

        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to approve application. Please try again.");
            return "redirect:/admin/applications/jobs/";
        }
    }


    @PostMapping("/{id}/deny")
    public String denyApplication(@PathVariable("id") Long applicantId,
                                  RedirectAttributes redirectAttributes) {
        try {
            Application app = applicationRepository.findById(applicantId)
                               .orElseThrow(() -> new RuntimeException("Application not found"));

            // If application was approved, restore vacancy
            if ("APPROVED".equals(app.getStatus())) {
                Job job = app.getJob();
                job.setVacancies(job.getVacancies() + 1);
                jobRepository.save(job);
            }

            // Set status to DENIED
            app.setStatus("DENIED");
            applicationRepository.save(app);

            notificationService.createNotification("Your application for '" + app.getJob().getTitle() + "' has been denied.", app.getUser());

            try {
                notifyApplicant(app, "We regret to inform you that your application for "
                        + app.getJob().getTitle() + " has been DENIED.");
                redirectAttributes.addFlashAttribute("successMessage", "Application denied and email sent.");
            } catch (Exception e) {
                e.printStackTrace();
                redirectAttributes.addFlashAttribute("warningMessage", "Application denied, but failed to send email.");
            }

            return "redirect:/admin/applications/jobs/" + app.getJob().getId();

        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to deny application. Please try again.");
            return "redirect:/admin/applications/jobs/";
        }
    }


    // Delete application
    @PostMapping("/{id}/delete")
    public String deleteApplication(@PathVariable Long id) {
        Application app = applicationRepository.findById(id).orElseThrow();
        Long jobId = app.getJob().getId();

        if ("APPROVED".equals(app.getStatus())) {
            Job job = app.getJob();
            job.setVacancies(job.getVacancies() + 1);
            jobRepository.save(job);
        }

        applicationRepository.delete(app);
        return "redirect:/admin/applications/jobs/" + jobId;
    }


    // View applicants for a job
    @GetMapping("/jobs/{jobId}/applicants")
    public String viewApplicants(@PathVariable Long jobId, Model model) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid job Id:" + jobId));

        List<MyUser> applicants = applicationRepository.findApplicantsByJobId(jobId);

        model.addAttribute("job", job);
        model.addAttribute("applicants", applicants);
        return "admin/view-applicants";
    }

    // Helper method to notify applicant by email
    private void notifyApplicant(Application app, String message) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(app.getEmail());
        email.setSubject("Job Application Update - " + app.getJob().getTitle());
        email.setText("Hello " + app.getCandidateName() + ",\n\n" + message);
        mailSender.send(email);
    }
}

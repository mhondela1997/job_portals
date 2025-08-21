package com.makanza.job_portals.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.makanza.job_portals.model.Application;
import com.makanza.job_portals.repository.ApplicationRepository;



@Controller
@RequestMapping("/my-applications")
public class ApplicantController {

    @Autowired
    private ApplicationRepository applicationRepository;

    // show logged-in applicant's applications
    @GetMapping
    public String viewMyApplications(Model model, Principal principal) {
        String email = principal.getName(); // Spring Security -> username is email
        List<Application> myApps = applicationRepository.findByEmail(email);
        model.addAttribute("applications", myApps);
        return "applicant/my-applications";
    }
}


package com.makanza.job_portals.controller;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.makanza.job_portals.model.Job;
import com.makanza.job_portals.model.MyUser;
import com.makanza.job_portals.model.Notification;
import com.makanza.job_portals.repository.ApplicationRepository;
import com.makanza.job_portals.repository.JobRepository;
import com.makanza.job_portals.repository.UserRepository;
import com.makanza.job_portals.service.NotificationService;
import com.makanza.job_portals.service.UserService;

import org.springframework.util.StringUtils;

@Controller
public class AuthController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("myuser", new MyUser());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("myuser") MyUser myuser) {
        // Encode password
        myuser.setPassword(passwordEncoder.encode(myuser.getPassword()));

        // Ensure role is uppercase and default to USER
        if (myuser.getRole() == null || myuser.getRole().isBlank()) {
            myuser.setRole("USER");
        } else {
            myuser.setRole(myuser.getRole().toUpperCase());
        }

        // Handle file upload from the transient field
        if (myuser.getPhotoFile() != null && !myuser.getPhotoFile().isEmpty()) {
            String filename = StringUtils.cleanPath(myuser.getPhotoFile().getOriginalFilename());
            Path uploadPath = Paths.get("uploads/");

            try {
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                try (InputStream inputStream = myuser.getPhotoFile().getInputStream()) {
                    Path filePath = uploadPath.resolve(filename);
                    Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
                }

                myuser.setPhoto(filename);

            } catch (IOException e) {
                e.printStackTrace();
                myuser.setPhoto(null); // fallback if upload fails
            }
        } else {
            myuser.setPhoto(null); // no file uploaded
        }

        // Save user to DB
        userRepository.save(myuser);

        return "redirect:/login?registered";
    }


    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, Principal principal) {
        long jobCount = jobRepository.count();
        long applicantCount = applicationRepository.countDistinctApplicants();
        long userCount = userRepository.count();

        model.addAttribute("jobCount", jobCount);
        model.addAttribute("applicantCount", applicantCount);
        model.addAttribute("userCount", userCount);

        List<Job> jobs = jobRepository.findAll();
        model.addAttribute("jobs", jobs);

        MyUser user = userService.findByUsername(principal.getName());
        model.addAttribute("user", user); 
        
        List<Notification> notifications = notificationService.findByUser(user);
        if (notifications == null) notifications = new ArrayList<>();

        model.addAttribute("notifications", notifications);

        return "dashboard";
    }

@PostMapping("/user/profile")
public String updateProfile(@ModelAttribute("user") MyUser updatedUser,
                            @RequestParam("photoFile") MultipartFile photoFile,
                            Principal principal) {
    MyUser currentUser = userService.findByUsername(principal.getName());
    
    currentUser.setEmail(updatedUser.getEmail());
    currentUser.setUsername(updatedUser.getUsername());

    // Handle photo upload if present
    if (photoFile != null && !photoFile.isEmpty()) {
        try {
            String filename = StringUtils.cleanPath(photoFile.getOriginalFilename());
            Path uploadPath = Paths.get("uploads/");
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            Files.copy(photoFile.getInputStream(), uploadPath.resolve(filename), StandardCopyOption.REPLACE_EXISTING);
            currentUser.setPhoto(filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Role is not editable
    userService.save(currentUser);
    return "redirect:/user/profile?success";
}
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/user/profile")
    public String profile(Model model, Principal principal) {
        MyUser user = userService.findByUsername(principal.getName());
        model.addAttribute("user", user);
        return "profile";
    }
    
    @GetMapping("/user/profile/edit")
    public String editProfile(Model model, Principal principal) {
        MyUser user = userService.findByUsername(principal.getName());
        model.addAttribute("user", user);
        return "edit-profile"; // edit form
    }
}

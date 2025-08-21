package com.makanza.job_portals.config;


import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.makanza.job_portals.model.MyUser;
import com.makanza.job_portals.repository.UserRepository;

import jakarta.annotation.PostConstruct;

@Component
public class DatabaseInit {
    @Autowired 
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @PostConstruct
    public void init() {
        if (userRepository.findByUsername("admin").isEmpty()) {
            MyUser admin = new MyUser();
            admin.setUsername("admin");
            admin.setEmail("admin@gmail.com");
            admin.setPassword(passwordEncoder.encode("admin@123"));
            admin.setRole("ADMIN");

            // Always set the photo filename, independent of copy success
            admin.setPhoto("mhondela.jpg");

            try {
                Path uploadPath = Paths.get("uploads/");
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                try (InputStream is = getClass().getResourceAsStream("/static/images/mhondela.jpg")) {
                    if (is != null) {
                        Files.copy(is, uploadPath.resolve("mhondela.jpg"), StandardCopyOption.REPLACE_EXISTING);
                    } else {
                        System.out.println("Admin default photo not found in resources!");
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            userRepository.save(admin);
        }
}
}

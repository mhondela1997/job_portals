package com.makanza.job_portals.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.makanza.job_portals.model.MyUser;
import com.makanza.job_portals.repository.UserRepository;

@Controller

public class ForgotPasswordController {
@Autowired
private PasswordEncoder passwordEncoder;
@Autowired
private UserRepository userRepository;

@GetMapping("/forgot-password")
public String showForgotForm() {
	return "forgot-password";
}

@PostMapping("/forgot-password")
public String processForgotPassword(@RequestParam String username, Model model) {
	Optional<MyUser> user=userRepository.findByUsername(username);
	if(user.isPresent()) {
		model.addAttribute("username", username);
		return "reset-password";
	}else {
		 model.addAttribute("error", "No account found with that username.");
         return "forgot-password";
	}
}

@PostMapping("/reset-password")
public String resetPassword(@RequestParam String username,
                            @RequestParam String newPassword,
                            Model model) {
    Optional<MyUser> userOpt = userRepository.findByUsername(username);
    if (userOpt.isPresent()) {
        MyUser user = userOpt.get();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        // Redirect with success param
        return "redirect:/login?resetSuccess";
    }
    model.addAttribute("error", "User not found.");
    return "reset-password";
}

}

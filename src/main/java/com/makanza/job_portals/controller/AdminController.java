package com.makanza.job_portals.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.makanza.job_portals.model.MyUser;
import com.makanza.job_portals.repository.UserRepository;

@Controller
@RequestMapping("/admin/users")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public String listUsers(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "users";  // users.html
    }

    @GetMapping("/edit/{id}")
    public String editUser(@PathVariable Long id, Model model) {
        MyUser user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        model.addAttribute("user", user);
        return "edit-user";  // edit-user.html
    }

    @PostMapping("/update")
    public String updateUser(@ModelAttribute("user") MyUser updatedUser) {
        MyUser user = userRepository.findById(updatedUser.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setRole(updatedUser.getRole()); // update only role
        userRepository.save(user);
        return "redirect:/admin/users?success";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
        return "redirect:/admin/users?deleted";
    }
}

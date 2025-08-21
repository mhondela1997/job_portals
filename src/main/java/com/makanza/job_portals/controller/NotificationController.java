package com.makanza.job_portals.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.makanza.job_portals.model.MyUser;
import com.makanza.job_portals.model.Notification;
import com.makanza.job_portals.service.NotificationService;
import com.makanza.job_portals.service.UserService;

@Controller
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;
    private final UserService userService;

    public NotificationController(NotificationService notificationService, UserService userService) {
        this.notificationService = notificationService;
        this.userService = userService;
    }

    @GetMapping
    public String notificationsPage(Model model, Principal principal) {
        MyUser user = userService.findByUsername(principal.getName());
        List<Notification> notifications = notificationService.getUserNotifications(user);
        model.addAttribute("notifications", notifications);
        return "dashboard"; // or separate notifications page
    }

    @PostMapping("/seen-all")
    @ResponseBody
    public void markAllSeen(Principal principal) {
        MyUser user = userService.findByUsername(principal.getName());
        notificationService.markAllAsSeen(user);
    }

    @PostMapping("/{id}/seen")
    @ResponseBody
    public void markAsSeen(@PathVariable Long id) {
        notificationService.markAsSeen(id);
    }
}
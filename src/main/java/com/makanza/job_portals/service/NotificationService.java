package com.makanza.job_portals.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.makanza.job_portals.model.MyUser;
import com.makanza.job_portals.model.Notification;
import com.makanza.job_portals.repository.NotificationRepository;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public List<Notification> getUserNotifications(MyUser user) {
        return notificationRepository.findByUserOrderByCreatedAtDesc(user);
    }

    public List<Notification> getUnreadNotifications(MyUser user) {
        return notificationRepository.findByUserAndSeenFalse(user);
    }

    public Notification createNotification(String message, MyUser user) {
        Notification notification = new Notification(message, user);
        return notificationRepository.save(notification);
    }

    public void markAllAsSeen(MyUser user) {
        List<Notification> notifications = getUnreadNotifications(user);
        notifications.forEach(n -> n.setSeen(true));
        notificationRepository.saveAll(notifications);
    }

    public void markAsSeen(Long notificationId) {
        notificationRepository.findById(notificationId).ifPresent(n -> {
            n.setSeen(true);
            notificationRepository.save(n);
        });
    }
    public List<Notification> findByUser(MyUser user) {
        List<Notification> notifications = notificationRepository.findByUser(user);
        if (notifications == null) {
            notifications = new ArrayList<>(); // return empty list instead of null
        }
        return notifications;
    }
}
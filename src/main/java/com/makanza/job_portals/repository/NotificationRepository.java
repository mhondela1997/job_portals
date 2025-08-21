package com.makanza.job_portals.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.makanza.job_portals.model.MyUser;
import com.makanza.job_portals.model.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserOrderByCreatedAtDesc(MyUser user);
    List<Notification> findByUserAndSeenFalse(MyUser user);
    List<Notification> findByUser(MyUser user);
}
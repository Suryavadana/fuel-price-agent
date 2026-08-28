package com.fuelagent.backend.repository;

import com.fuelagent.backend.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification>findByOwnerIdOrderByCreatedAtDesc(String ownerId);
}

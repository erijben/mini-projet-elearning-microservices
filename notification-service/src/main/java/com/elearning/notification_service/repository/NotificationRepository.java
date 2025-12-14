package com.elearning.notification_service.repository;

import com.elearning.notification_service.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByStudentId(Long studentId);
    List<Notification> findByCourseId(Long courseId);
}

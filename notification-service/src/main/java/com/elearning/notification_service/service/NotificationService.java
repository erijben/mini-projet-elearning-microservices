package com.elearning.notification_service.service;

import com.elearning.notification_service.model.Notification;
import com.elearning.notification_service.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {
    private final NotificationRepository repo;

    public NotificationService(NotificationRepository repo) {
        this.repo = repo;
    }

    public Notification saveNotification(Notification n) {
        return repo.save(n);
    }

    public List<Notification> findByStudentId(Long studentId){
        return repo.findByStudentId(studentId);
    }

    public List<Notification> findByCourseId(Long courseId){
        return repo.findByCourseId(courseId);
    }

    public List<Notification> findAll(){
        return repo.findAll();
    }
}

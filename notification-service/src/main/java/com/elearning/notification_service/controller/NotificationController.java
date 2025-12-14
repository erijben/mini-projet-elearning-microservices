package com.elearning.notification_service.controller;

import com.elearning.notification_service.model.Notification;
import com.elearning.notification_service.repository.NotificationRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {
    private final NotificationRepository repo;

    public NotificationController(NotificationRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Notification> all() {
        return repo.findAll();
    }

    @GetMapping("/by-student/{studentId}")
    public List<Notification> byStudent(@PathVariable Long studentId) {
        return repo.findByStudentId(studentId);
    }

    @GetMapping("/by-course/{courseId}")
    public List<Notification> byCourse(@PathVariable Long courseId) {
        return repo.findByCourseId(courseId);
    }
}

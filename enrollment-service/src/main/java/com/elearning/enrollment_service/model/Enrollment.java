package com.elearning.enrollment_service.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Enrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long studentId;
    private Long courseId;
    private LocalDateTime enrolledAt;

    // Constructors
    public Enrollment(){}

    public Enrollment(Long studentId, Long courseId) {
        this.studentId = studentId;
        this.courseId = courseId;
        this.enrolledAt = LocalDateTime.now();
    }

    // getters / setters
    public Long getId(){ return id; }
    public void setId(Long id){ this.id = id; }
    public Long getStudentId(){ return studentId; }
    public void setStudentId(Long studentId){ this.studentId = studentId; }
    public Long getCourseId(){ return courseId; }
    public void setCourseId(Long courseId){ this.courseId = courseId; }
    public LocalDateTime getEnrolledAt(){ return enrolledAt; }
    public void setEnrolledAt(LocalDateTime enrolledAt){ this.enrolledAt = enrolledAt; }
}

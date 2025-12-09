package com.elearning.courseservice.controller;

import com.elearning.courseservice.model.Course;
import com.elearning.courseservice.repository.CourseRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/courses")
public class CourseController {

    private final CourseRepository courseRepository;

    public CourseController(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    // GET /courses
    @GetMapping
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    // POST /courses
    @PostMapping
    public ResponseEntity<Course> createCourse(@Valid @RequestBody Course course) {
        Course saved = courseRepository.save(course);
        return ResponseEntity
                .created(URI.create("/courses/" + saved.getId()))
                .body(saved);
    }
}

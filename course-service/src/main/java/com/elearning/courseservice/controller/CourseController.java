package com.elearning.courseservice.controller;

import com.elearning.courseservice.controller.dto.CreateCourseRequest;
import com.elearning.courseservice.model.Course;
import com.elearning.courseservice.service.CourseService;
import jakarta.validation.Valid;                    // ✅ important
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    // ---------- CREATE ----------
    @PostMapping
    public ResponseEntity<Course> createCourse(@Valid @RequestBody CreateCourseRequest request) {
        Course saved = courseService.createCourse(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // ---------- READ ALL ----------
    @GetMapping
    public List<Course> getAllCourses() {
        return courseService.getAllCourses();
    }

    // ---------- READ BY ID ----------
    @GetMapping("/{id}")
    public ResponseEntity<Course> getCourseById(@PathVariable Long id) {
        try {
            Course course = courseService.getCourseById(id);
            return ResponseEntity.ok(course);
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    // ---------- UPDATE ----------
    @PutMapping("/{id}")
    public ResponseEntity<Course> updateCourse(
            @PathVariable Long id,
            @Valid @RequestBody CreateCourseRequest request
    ) {
        try {
            Course updated = courseService.updateCourse(id, request);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    // ---------- DELETE ----------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        try {
            courseService.deleteCourse(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
    }
}

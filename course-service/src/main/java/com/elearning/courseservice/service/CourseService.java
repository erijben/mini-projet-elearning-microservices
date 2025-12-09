package com.elearning.courseservice.service;

import com.elearning.courseservice.controller.dto.CreateCourseRequest;
import com.elearning.courseservice.model.Course;
import com.elearning.courseservice.model.Module;
import com.elearning.courseservice.repository.CourseRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CourseService {

    private final CourseRepository courseRepository;

    // -------- CREATE --------
    public Course createCourse(CreateCourseRequest request) {

        Course course = Course.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .teacherId(request.getTeacherId())
                .level(request.getLevel())
                .build();

        // créer les modules (si tu en as dans la requête)
        if (request.getModules() != null) {
            request.getModules().forEach(mr -> {
                Module module = Module.builder()
                        .title(mr.getTitle())
                        .content(mr.getContent())
                        .build();
                course.addModule(module);
            });
        }

        return courseRepository.save(course);
    }

    // -------- READ ALL --------
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    // -------- READ BY ID --------
    public Course getCourseById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + id));
    }

    // -------- UPDATE --------
    public Course updateCourse(Long id, CreateCourseRequest request) {

        Course course = getCourseById(id); // lève une exception si non trouvé

        course.setTitle(request.getTitle());
        course.setDescription(request.getDescription());
        course.setTeacherId(request.getTeacherId());
        course.setLevel(request.getLevel());

        // on remplace complètement les modules
        course.getModules().clear();
        if (request.getModules() != null) {
            request.getModules().forEach(mr -> {
                Module module = Module.builder()
                        .title(mr.getTitle())
                        .content(mr.getContent())
                        .build();
                course.addModule(module);
            });
        }

        return courseRepository.save(course);
    }

    // -------- DELETE --------
    public void deleteCourse(Long id) {
        Course course = getCourseById(id); // lèvera RuntimeException si non trouvé
        courseRepository.delete(course);
    }
}

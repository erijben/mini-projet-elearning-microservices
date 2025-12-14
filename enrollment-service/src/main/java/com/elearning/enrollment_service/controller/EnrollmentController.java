package com.elearning.enrollment_service.controller;

import com.elearning.enrollment_service.model.Enrollment;
import com.elearning.enrollment_service.repository.EnrollmentRepository;
import com.elearning.enrollment_service.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/enrollments")
public class EnrollmentController {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private JwtUtils jwtUtils; // celui de enrollment-service

    @PostMapping
    public ResponseEntity<?> enroll(@RequestHeader(value = "Authorization", required = false) String authHeader,
                                    @RequestBody Map<String, Long> body) {
        try {
            String token = JwtUtils.tokenFromHeader(authHeader);
            if (token == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing Authorization header");
            }
            Long studentId = jwtUtils.extractUserId(token); // parse & vérifie la signature
            Long courseId = body.get("courseId");
            if (courseId == null) return ResponseEntity.badRequest().body("courseId required");

            Enrollment e = new Enrollment();
            e.setStudentId(studentId);
            e.setCourseId(courseId);
            e.setEnrolledAt(LocalDateTime.now());

            enrollmentRepository.save(e);
            return ResponseEntity.ok(e);

        } catch (io.jsonwebtoken.security.SignatureException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token signature");
        } catch (io.jsonwebtoken.ExpiredJwtException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token expired");
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server error: " + ex.getMessage());
        }
    }
    // récupérer mes inscriptions
    @GetMapping("/me")
    public ResponseEntity<?> myEnrollments(@RequestHeader("Authorization") String authHeader) {
        String token = JwtUtils.tokenFromHeader(authHeader);
        if (token == null) return ResponseEntity.status(401).body("Missing token");
        Long studentId = jwtUtils.extractUserId(token);
        List<Enrollment> list = enrollmentRepository.findByStudentId(studentId);
        return ResponseEntity.ok(list);
    }
}


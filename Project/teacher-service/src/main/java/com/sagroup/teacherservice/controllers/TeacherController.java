package com.sagroup.teacherservice.controllers;

import com.sagroup.teacherservice.domains.Teacher;
import com.sagroup.teacherservice.services.TeacherService;
import com.sagroup.teacherservice.dto.TeacherDTO;
import com.sagroup.teacherservice.dto.TeacherResponseDTO;
import com.sagroup.teacherservice.dto.TeachingAssignmentDTO;
import com.sagroup.teacherservice.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/teacher")
@Slf4j
@CrossOrigin(origins = "http://localhost:3000")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private JwtUtils jwtUtils;

    private ResponseEntity<?> handleValidationErrors(BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errors = result.getFieldErrors().stream()
                    .collect(Collectors.toMap(
                            FieldError::getField,
                            FieldError::getDefaultMessage,
                            (error1, error2) -> error1
                    ));
            log.error("Validation errors: {}", errors);
            return ResponseEntity.badRequest().body(errors);
        }
        return null;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTeacher(@PathVariable Long id) {
        try {
            return teacherService.getTeacher(id)
                    .map(teacher -> {
                        TeacherResponseDTO response = TeacherResponseDTO.builder()
                            .teacherID(teacher.getTeacherID())
                            .fullName(teacher.getFullName())
                            .email(teacher.getEmail())
                            .phone(teacher.getPhone())
                            .address(teacher.getAddress())
                            .homeroomClass(teacher.getHomeroomClass())
                            .subject(teacher.getSubject())
                            .teachingClasses(teacher.getTeachingClasses())
                            .build();
                        return ResponseEntity.ok(response);
                    })
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            log.error("Error fetching teacher with id {}: {}", id, e.getMessage());
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Failed to fetch teacher details"));
        }
    }

    @GetMapping("/class/{className}")
    public ResponseEntity<List<Teacher>> getTeachersByClass(@PathVariable String className) {
        try {
            return ResponseEntity.ok(teacherService.getTeachersByClass(className));
        } catch (Exception e) {
            log.error("Error fetching teachers by class {}: {}", className, e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/subject/{subject}")
    public ResponseEntity<List<Teacher>> getTeachersBySubject(@PathVariable String subject) {
        try {
            return ResponseEntity.ok(teacherService.getTeachersBySubject(subject));
        } catch (Exception e) {
            log.error("Error fetching teachers by subject {}: {}", subject, e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/addTeacher")
    public ResponseEntity<?> addTeacher(@Valid @RequestBody TeacherDTO teacherDTO, BindingResult result) {
        log.info("Received request to add teacher: {}", teacherDTO);
        
        ResponseEntity<?> errors = handleValidationErrors(result);
        if (errors != null) {
            log.error("Validation errors in request");
            return errors;
        }

        try {
            Teacher saved = teacherService.add(teacherDTO);
            log.info("Successfully added teacher with ID: {}", saved.getTeacherID());
            
            TeacherResponseDTO response = TeacherResponseDTO.builder()
                .teacherID(saved.getTeacherID())
                .fullName(saved.getFullName())
                .email(saved.getEmail())
                .phone(saved.getPhone())
                .address(saved.getAddress())
                .homeroomClass(saved.getHomeroomClass())
                .subject(saved.getSubject())
                .teachingClasses(saved.getTeachingClasses())
                .build();
                
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error adding teacher: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateTeacher(@PathVariable Long id, @Valid @RequestBody TeacherDTO teacherDTO, BindingResult result) {
        ResponseEntity<?> errors = handleValidationErrors(result);
        if (errors != null) return errors;

        try {
            Teacher updated = teacherService.update(id, teacherDTO);
            TeacherResponseDTO response = TeacherResponseDTO.builder()
                .teacherID(updated.getTeacherID())
                .fullName(updated.getFullName())
                .email(updated.getEmail())
                .phone(updated.getPhone())
                .address(updated.getAddress())
                .homeroomClass(updated.getHomeroomClass())
                .subject(updated.getSubject())
                .teachingClasses(updated.getTeachingClasses())
                .build();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error updating teacher with id {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/getTeachers")
    public ResponseEntity<List<TeacherResponseDTO>> getAllTeachers() {
        List<Teacher> teachers = teacherService.getAll();
        List<TeacherResponseDTO> response = teachers.stream()
            .map(teacher -> TeacherResponseDTO.builder()
                .teacherID(teacher.getTeacherID())
                .fullName(teacher.getFullName())
                .email(teacher.getEmail())
                .phone(teacher.getPhone())
                .address(teacher.getAddress())
                .homeroomClass(teacher.getHomeroomClass())
                .subject(teacher.getSubject())
                .teachingClasses(teacher.getTeachingClasses())
                .build())
            .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteTeacher(@PathVariable Long id) {
        try {
            String result = teacherService.delete(id);
            if ("Teacher not found".equals(result)) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Error deleting teacher with id {}: {}", id, e.getMessage());
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Failed to delete teacher"));
        }
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> getCurrentTeacher(HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization").substring(7);
            String username = jwtUtils.getUsernameFromToken(token);
            
            return teacherService.findByEmail(username)
                    .map(teacher -> {
                        TeacherResponseDTO response = TeacherResponseDTO.builder()
                            .teacherID(teacher.getTeacherID())
                            .fullName(teacher.getFullName())
                            .email(teacher.getEmail())
                            .phone(teacher.getPhone())
                            .address(teacher.getAddress())
                            .homeroomClass(teacher.getHomeroomClass())
                            .subject(teacher.getSubject())
                            .teachingClasses(teacher.getTeachingClasses())
                            .build();
                        return ResponseEntity.ok(response);
                    })
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            log.error("Error fetching current teacher: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Failed to get teacher information"));
        }
    }
}


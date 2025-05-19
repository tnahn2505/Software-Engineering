package com.sagroup.studentservice.controllers;

import com.sagroup.studentservice.domains.Student;
import com.sagroup.studentservice.dto.NewUserRequest;
import com.sagroup.studentservice.dto.ScoreUpdateDTO;
import com.sagroup.studentservice.services.StudentService;
import com.sagroup.studentservice.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/student")
public class studentController {

    @Autowired
    private StudentService studentService;

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
            return ResponseEntity.badRequest().body(errors);
        }
        return null;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudent(@PathVariable Long id) {
        return studentService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/scores/{id}")
    public ResponseEntity<Student> getStudentScores(@PathVariable Long id) {
        return studentService.getStudentWithScores(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/byClass/{className}")
    public ResponseEntity<List<Student>> getByClass(@PathVariable String className) {
        return ResponseEntity.ok(studentService.getByClassName(className));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/addStudent")
    public ResponseEntity<?> addStudent(@Valid @RequestBody Student student, BindingResult result) {
        ResponseEntity<?> errors = handleValidationErrors(result);
        if (errors != null) return errors;

        try {
            Student saved = studentService.addStudent(student);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody Student student, BindingResult result) {
        ResponseEntity<?> errors = handleValidationErrors(result);
        if (errors != null) return errors;

        try {
            return ResponseEntity.ok(studentService.update(id, student));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/getStudents")
    public ResponseEntity<List<Student>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> remove(@PathVariable Long id) {
        studentService.removeById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Map<String, Long>>> getStats() {
        return ResponseEntity.ok(studentService.getPerformanceStatisticsByClass());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @PutMapping("/updateScore/{id}")
    public ResponseEntity<?> updateScore(@PathVariable Long id, @Valid @RequestBody ScoreUpdateDTO scoreUpdateDTO, BindingResult result) {
        ResponseEntity<?> errors = handleValidationErrors(result);
        if (errors != null) return errors;

        try {
            Student updated = studentService.updateScore(id, scoreUpdateDTO);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> getCurrentStudent(HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization").substring(7);
            String username = jwtUtils.getUsernameFromToken(token);
            
            // Find student by email (username)
            Optional<Student> student = studentService.findByEmail(username);
            if (student.isPresent()) {
                return ResponseEntity.ok(student.get());
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to get student information"));
        }
    }

    // Nếu muốn phát triển sau:
    // @PostMapping("/buy")
    // public ResponseEntity<?> buyAvatar(...) { ... }

}

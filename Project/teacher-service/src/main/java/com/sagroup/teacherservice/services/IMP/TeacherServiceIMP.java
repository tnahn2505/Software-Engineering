package com.sagroup.teacherservice.services.IMP;

import com.sagroup.teacherservice.domains.Teacher;
import com.sagroup.teacherservice.domains.TeachingAssignment;
import com.sagroup.teacherservice.repositories.TeacherRepository;
import com.sagroup.teacherservice.services.TeacherService;
import com.sagroup.teacherservice.dto.NewUserRequest;
import com.sagroup.teacherservice.dto.TeacherDTO;
import com.sagroup.teacherservice.dto.TeachingAssignmentDTO;
import com.sagroup.teacherservice.domains.Role;
import com.sagroup.teacherservice.domains.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Optional;


@Service
@Primary
public class TeacherServiceIMP implements TeacherService {

    private static final Logger log = LoggerFactory.getLogger(TeacherServiceIMP.class);

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private RestTemplate restTemplate;

    private String getAuthToken() {
        try {
            String loginUrl = "http://localhost:8089/users/auth/login";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            Map<String, String> loginRequest = Map.of(
                "username", "admin",
                "password", "123456"
            );

            HttpEntity<Map<String, String>> request = new HttpEntity<>(loginRequest, headers);

            @SuppressWarnings("unchecked")
            Map<String, Object> responseBody = restTemplate.postForEntity(loginUrl, request, Map.class).getBody();

            if (responseBody != null) {
                return (String) responseBody.get("token");
            }
            throw new RuntimeException("Failed to get authentication token");
        } catch (Exception e) {
            throw new RuntimeException("Failed to get authentication token: " + e.getMessage());
        }
    }

    @Override
    public List<Teacher> getAll() {
        return teacherRepository.findAll();
    }

    @Override
    public Optional<Teacher> getTeacher(Long id) {
        return teacherRepository.findById(id);
    }

    @Override
    @Transactional
    public Teacher add(TeacherDTO teacherDTO) {
        try {
            log.info("Starting to create teacher from DTO: {}", teacherDTO);
            
            // Check if email already exists in user-service
            String checkEmailUrl = "http://localhost:8089/users/check-email?email=" + teacherDTO.getEmail();
            HttpHeaders checkHeaders = new HttpHeaders();
            checkHeaders.setBearerAuth(getAuthToken());
            HttpEntity<Void> checkEntity = new HttpEntity<>(checkHeaders);
            ResponseEntity<Boolean> checkResponse = restTemplate.exchange(checkEmailUrl, HttpMethod.GET, checkEntity, Boolean.class);
            Boolean emailExists = checkResponse.getBody();
            if (Boolean.TRUE.equals(emailExists)) {
                log.error("Email {} already exists in user-service", teacherDTO.getEmail());
                throw new RuntimeException("Email already exists in the system");
            }

            Teacher teacher = new Teacher();
            teacher.setFullName(teacherDTO.getFullName());
            teacher.setAddress(teacherDTO.getAddress());
            teacher.setEmail(teacherDTO.getEmail());
            teacher.setPhone(teacherDTO.getPhone());
            teacher.setHomeroomClass(teacherDTO.getHomeroomClass());
            teacher.setPassword("123456"); // Default password
            teacher.setRole(Role.TEACHER); // Set role as TEACHER
            teacher.setSubject(teacherDTO.getSubject());
            teacher.setTeachingClasses(teacherDTO.getTeachingClasses());

            log.info("Saving teacher to database");
            Teacher savedTeacher = teacherRepository.save(teacher);
            log.info("Teacher saved successfully with ID: {}", savedTeacher.getTeacherID());

            // Create user account
            try {
                log.info("Creating user account for teacher with email: {}", teacher.getEmail());
                NewUserRequest userRequest = new NewUserRequest(
                    teacher.getEmail(),
                    "TEACHER",
                    "123456" // Default password
                );

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.setBearerAuth(getAuthToken());

                HttpEntity<NewUserRequest> requestEntity = new HttpEntity<>(userRequest, headers);

                log.info("Sending request to user service");
                ResponseEntity<String> response = restTemplate.exchange(
                    "http://localhost:8089/users/create",
                    HttpMethod.POST,
                    requestEntity,
                    String.class
                );

                if (!response.getStatusCode().is2xxSuccessful()) {
                    log.error("Failed to create user account. Status: {}, Response: {}", 
                        response.getStatusCode(), response.getBody());
                    throw new RuntimeException("Failed to create user account: " + response.getBody());
                }

                log.info("User account created successfully");
                return savedTeacher;

            } catch (Exception e) {
                log.error("Error creating user account: {}", e.getMessage(), e);
                throw new RuntimeException("Failed to create user account: " + e.getMessage());
            }
        } catch (Exception e) {
            log.error("Error in add method: {}", e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Teacher update(Long id, TeacherDTO teacherDTO) {
        Teacher existingTeacher = teacherRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Teacher not found with id: " + id));

        existingTeacher.setFullName(teacherDTO.getFullName());
        existingTeacher.setAddress(teacherDTO.getAddress());
        existingTeacher.setEmail(teacherDTO.getEmail());
        existingTeacher.setPhone(teacherDTO.getPhone());
        existingTeacher.setHomeroomClass(teacherDTO.getHomeroomClass());
        existingTeacher.setSubject(teacherDTO.getSubject());
        existingTeacher.setTeachingClasses(teacherDTO.getTeachingClasses());

        return teacherRepository.save(existingTeacher);
    }

    @Override
    public String delete(Long id) {
        Teacher teacher = teacherRepository.findById(id).orElse(null);
        if (teacher != null) {
            teacherRepository.deleteById(id);
            return "Safely deleted";
        }
        return "Teacher not found";
    }

    @Override
    public List<Teacher> getTeachersByClass(String className) {
        return teacherRepository.findAll().stream()
            .filter(teacher -> className.equals(teacher.getHomeroomClass()))
            .collect(Collectors.toList());
    }

    @Override
    public List<Teacher> getTeachersBySubject(String subject) {
        try {
            Subject subjectEnum = Subject.valueOf(subject.toUpperCase());
            return teacherRepository.findAll().stream()
                .filter(teacher -> teacher.getSubject() == subjectEnum)
                .collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            log.error("Invalid subject value: {}", subject);
            return List.of();
        }
    }

    @Override
    public Optional<Teacher> findByEmail(String email) {
        return teacherRepository.findByEmail(email);
    }
}

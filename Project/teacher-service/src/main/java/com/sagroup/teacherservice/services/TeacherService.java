package com.sagroup.teacherservice.services;

import com.sagroup.teacherservice.domains.Teacher;
import com.sagroup.teacherservice.dto.TeacherDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface TeacherService {
    List<Teacher> getAll();
    Optional<Teacher> getTeacher(Long id);
    Teacher add(TeacherDTO teacherDTO);
    Teacher update(Long id, TeacherDTO teacherDTO);
    String delete(Long id);
    List<Teacher> getTeachersByClass(String className);
    List<Teacher> getTeachersBySubject(String subject);
    Optional<Teacher> findByEmail(String email);
}

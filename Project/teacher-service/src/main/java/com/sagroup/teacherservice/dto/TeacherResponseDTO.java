package com.sagroup.teacherservice.dto;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.sagroup.teacherservice.domains.Subject;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeacherResponseDTO {
    private Long teacherID;
    private String fullName;
    private String email;
    private String phone;
    private String address;
    private String homeroomClass;
    private Subject subject;
    private List<String> teachingClasses;
} 
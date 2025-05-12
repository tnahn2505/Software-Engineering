package com.sagroup.teacherservice.dto;

import com.sagroup.teacherservice.domains.Subject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeachingAssignmentDTO {
    @NotBlank(message = "Tên lớp không được để trống")
    private String className;
    
    @NotBlank(message = "Môn học không được để trống")
    private Subject subject;
} 
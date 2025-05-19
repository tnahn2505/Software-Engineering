package com.sagroup.teacherservice.dto;

import lombok.Data;
import jakarta.validation.constraints.*;
import java.util.List;
import com.sagroup.teacherservice.domains.Subject;

@Data
public class TeacherDTO {
    @NotBlank(message = "Họ và tên không được để trống")
    @Pattern(regexp = "^[A-ZÀÁẠẢÃÂẦẤẬẨẪĂẰẮẶẲẴÈÉẸẺẼÊỀẾỆỂỄÌÍỊỈĨÒÓỌỎÕÔỒỐỘỔỖƠỜỚỢỞỠÙÚỤỦŨƯỪỨỰỬỮỲÝỴỶỸĐ][a-zàáạảãâầấậẩẫăằắặẳẵèéẹẻẽêềếệểễìíịỉĩòóọỏõôồốộổỗơờớợởỡùúụủũưừứựửữỳýỵỷỹđ]*(?:[ ][A-ZÀÁẠẢÃÂẦẤẬẨẪĂẰẮẶẲẴÈÉẸẺẼÊỀẾỆỂỄÌÍỊỈĨÒÓỌỎÕÔỒỐỘỔỖƠỜỚỢỞỠÙÚỤỦŨƯỪỨỰỬỮỲÝỴỶỸĐ][a-zàáạảãâầấậẩẫăằắặẳẵèéẹẻẽêềếệểễìíịỉĩòóọỏõôồốộổỗơờớợởỡùúụủũưừứựửữỳýỵỷỹđ]*)*$", 
            message = "Họ và tên phải viết hoa chữ cái đầu")
    private String fullName;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", 
            message = "Email phải đúng định dạng (ví dụ: example@gmail.com)")
    private String email;

    @Pattern(regexp = "^(0|\\+84)(3|5|7|8|9)[0-9]{8}$", 
            message = "Số điện thoại không hợp lệ (phải bắt đầu bằng 0 hoặc +84 và có 10 chữ số)")
    private String phone;

    private String address;
    private String homeroomClass;

    @NotNull(message = "Môn giảng dạy không được để trống")
    private Subject subject;

    private List<String> teachingClasses;
} 
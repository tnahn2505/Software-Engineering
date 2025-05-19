package com.sagroup.studentservice.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class NewUserRequest {
    private String email;
    private String role;
    private String password;

    public NewUserRequest() {}

    public NewUserRequest(String email, String role, String password) {
        this.email = email;
        this.role = role;
        this.password = password;
    }
}

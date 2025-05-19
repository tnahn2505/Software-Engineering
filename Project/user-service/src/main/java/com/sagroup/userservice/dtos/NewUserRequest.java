package com.sagroup.userservice.dtos;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class NewUserRequest {
    private String email;
    private String role;
    private String password;
}


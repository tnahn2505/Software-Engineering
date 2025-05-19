package com.sagroup.userservice.dtos;

import lombok.Data;

@Data
public class NewAppUserDto {
    private String id;
    private String username;
    private String password;
    private String role;
    private String email;
}

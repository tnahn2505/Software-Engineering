package com.sagroup.userservice.controller;

import com.sagroup.userservice.dtos.NewAppUserDto;
import com.sagroup.userservice.dtos.NewUserRequest;
import com.sagroup.userservice.dtos.ResetPasswordRequest;
import com.sagroup.userservice.dtos.ChangePasswordRequest;
import com.sagroup.userservice.entity.NewAppUser;
import com.sagroup.userservice.service.NewAppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.sagroup.userservice.repository.NewAppUserRepository;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/users")
public class NewAppUserController {

    private NewAppUserService userService;
    @Autowired
    private NewAppUserRepository userRepository;

    @GetMapping("/view")
    public List<NewAppUser> viewAll(){
        return userService.viewAll();
    }

    @PostMapping
    public ResponseEntity<?> add(@RequestBody NewAppUserDto userDto){
        NewAppUser user = userService.save(userDto);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/auth/request-reset")
    public ResponseEntity<?> requestReset(@RequestParam String email) {
        userService.generateResetToken(email);
        return ResponseEntity.ok("Reset token sent to email.");
    }

    @PostMapping("/auth/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {
        userService.resetPassword(request.getToken(), request.getNewPassword());
        return ResponseEntity.ok("Password reset successfully.");
    }

    @PostMapping("/auth/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request, Authentication authentication) {
        try {
            String username = authentication.getName();
            userService.changePassword(username, request.getCurrentPassword(), request.getNewPassword());
            return ResponseEntity.ok("Password changed successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody NewAppUserDto userDto){
        NewAppUser user = userService.update(userDto);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public void remove(@PathVariable String id){
        userService.removeById(id);
    }

    @Autowired
    public void setUserService(NewAppUserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createAccount(@RequestBody NewUserRequest request) {
        NewAppUserDto dto = new NewAppUserDto();
        dto.setEmail(request.getEmail());
        dto.setUsername(request.getEmail()); // hoặc tách riêng username
        dto.setRole(request.getRole());

        String rawPassword = (request.getPassword() == null || request.getPassword().isBlank()) ? "123456" : request.getPassword();
        dto.setPassword(rawPassword);

        try {
            NewAppUser user = userService.save(dto);
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/check-email")
    public ResponseEntity<Boolean> checkEmailExists(@RequestParam String email) {
        boolean exists = userRepository.existsByUsernameIgnoreCase(email);
        return ResponseEntity.ok(exists);
    }

}

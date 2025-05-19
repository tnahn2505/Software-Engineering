package com.sagroup.userservice.controller;

import com.sagroup.userservice.dtos.LoginRequest;
import com.sagroup.userservice.repository.NewAppUserRepository;
import com.sagroup.userservice.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/users/auth")
public class LoginController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private NewAppUserRepository userRepo;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
    try {
        System.out.println("Đăng nhập: " + request.getUsername());

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        var userDetails = (org.springframework.security.core.userdetails.User) auth.getPrincipal();

        var user = userRepo.findByUsername(request.getUsername());
        if (user.isEmpty()) {
            return ResponseEntity.status(404).body("Không tìm thấy người dùng");
        }

        String token = jwtUtils.generateJwtToken(userDetails.getUsername(), userDetails.getAuthorities());

        return ResponseEntity.ok(Map.of(
                "token", token,
                "role", user.get().getRole().name()
        ));
    } catch (Exception e) {
        System.out.println("Lỗi xác thực: " + e.getMessage());
        return ResponseEntity.status(401).body("Sai tên đăng nhập hoặc mật khẩu");
    }
}





}

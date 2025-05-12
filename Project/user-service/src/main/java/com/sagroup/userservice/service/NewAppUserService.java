package com.sagroup.userservice.service;

import com.sagroup.userservice.dtos.NewAppUserDto;
import com.sagroup.userservice.entity.NewAppUser;
import com.sagroup.userservice.entity.Role;
import com.sagroup.userservice.repository.NewAppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class NewAppUserService {

    private NewAppUserRepository userRepository;
    private SendEmail sendEmail;
    private PasswordEncoder passwordEncoder;

    public List<NewAppUser> viewAll() {
        return userRepository.findAll();
    }

    public void changePassword(String username, String currentPassword, String newPassword) {
        NewAppUser user = userRepository.findByUsernameIgnoreCase(username);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new RuntimeException("Current password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public NewAppUser update(NewAppUserDto userDto) {
        Long userId = Long.valueOf(userDto.getId());
        NewAppUser userToBeUpdated = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        userToBeUpdated.setUsername(userDto.getUsername());
        userToBeUpdated.setPassword(passwordEncoder.encode(userDto.getPassword()));

        // Convert String role to Role enum
        Role role = Role.valueOf(userDto.getRole().toUpperCase());  // Convert String role to Enum
        userToBeUpdated.setRole(role);

        return userRepository.save(userToBeUpdated);
    }

    public NewAppUser save(NewAppUserDto userDto) {
        NewAppUser user = new NewAppUser();
        user.setUsername(userDto.getUsername());
        String rawPassword = userDto.getPassword();
        user.setPassword(passwordEncoder.encode(rawPassword));

        // Convert String role to Role enum
        Role role = Role.valueOf(userDto.getRole().toUpperCase());  // Convert String role to Enum
        user.setRole(role);

        NewAppUser userCreated = userRepository.save(user);

        // Gửi email có chứa mật khẩu và vai trò
        sendEmail.sendAccountCreatedEmail(user.getUsername(), rawPassword, user.getRole().name());

        return userCreated;
    }

    public void removeById(String id) {
        Long userId = Long.valueOf(id);
        userRepository.deleteById(userId);
    }

    public NewAppUser login(String username, String password) {
        NewAppUser user = userRepository.findByUsernameIgnoreCase(username);
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            return null;
        }
        return user;
    }

    public void generateResetToken(String email) {
        NewAppUser user = userRepository.findByUsernameIgnoreCase(email);
        if (user == null) {
            throw new RuntimeException("Email not found");
        }

        String token = UUID.randomUUID().toString();
        user.setResetToken(token);
        userRepository.save(user);

        sendEmail.sendResetToken(email, token);
    }

    public void resetPassword(String token, String newPassword) {
        NewAppUser user = userRepository.findByResetToken(token);
        if (user == null) {
            throw new RuntimeException("Invalid token");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetToken(null);
        userRepository.save(user);
    }

    @Autowired
    public void setUserRepository(NewAppUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setSendEmail(SendEmail sendEmail) {
        this.sendEmail = sendEmail;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void initAdminUser() {
        if (userRepository.findByUsernameIgnoreCase("admin") == null) {
            NewAppUser admin = new NewAppUser();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("123456"));
            admin.setRole(Role.ADMIN);
            userRepository.save(admin);
        }
    }

}

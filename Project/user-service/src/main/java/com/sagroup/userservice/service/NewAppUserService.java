package com.sagroup.userservice.service;

import com.sagroup.userservice.dtos.NewAppUserDto;
import com.sagroup.userservice.entity.NewAppUser;
import com.sagroup.userservice.repository.NewAppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class NewAppUserService {

    private NewAppUserRepository userRepository;

    private SendEmail sendEmail;

    public List<NewAppUser> viewAll() {
        return userRepository.findAll();
    }

    public NewAppUser update(NewAppUserDto userDto) {
        // ID đang truyền là String -> cần convert sang Long
        Long userId = Long.valueOf(userDto.getId());
        NewAppUser userToBeUpdated = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        userToBeUpdated.setUsername(userDto.getUsername());
        userToBeUpdated.setPassword(userDto.getPassword());
        userToBeUpdated.setRole(userDto.getRole());

        return userRepository.save(userToBeUpdated);
    }

    public NewAppUser save(NewAppUserDto userDto) {
        NewAppUser user = new NewAppUser();
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        user.setRole(userDto.getRole());

        NewAppUser userCreated = userRepository.save(user);
        sendEmail.sendMail(user.getUsername());
        return userCreated;
    }

    public void removeById(String id) {
        Long userId = Long.valueOf(id); // Convert ID sang Long
        userRepository.deleteById(userId);
    }
    public boolean login(String username, String password) {
        System.out.println("LOGIN REQUEST: username=" + username + ", password=" + password); // log
        NewAppUser user = userRepository.findByUsernameIgnoreCase(username);
        if (user == null) return false;
        return user.getPassword().equals(password);
    }




    @Autowired
    public void setUserRepository(NewAppUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setSendEmail(SendEmail sendEmail) {
        this.sendEmail = sendEmail;
    }
}

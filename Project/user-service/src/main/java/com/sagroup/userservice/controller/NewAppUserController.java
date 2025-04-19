package com.sagroup.userservice.controller;

import com.sagroup.userservice.dtos.LoginRequest;
import com.sagroup.userservice.dtos.NewAppUserDto;
import com.sagroup.userservice.entity.NewAppUser;
import com.sagroup.userservice.service.NewAppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/users")
public class NewAppUserController {

    private NewAppUserService userService;
    @GetMapping("/view")
    public List<NewAppUser> viewAll(){
        return userService.viewAll();
    }

    @PostMapping
    public ResponseEntity<?> add(@RequestBody NewAppUserDto userDto){
        NewAppUser user = userService.save(userDto);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        System.out.println("USERNAME: " + request.getUsername());
        System.out.println("PASSWORD: " + request.getPassword());

        boolean success = userService.login(request.getUsername(), request.getPassword());

        if (success) {
            return ResponseEntity.ok("Login successful");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
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
}

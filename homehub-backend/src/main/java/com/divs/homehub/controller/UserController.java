package com.divs.homehub.controller;

import com.divs.homehub.dto.RegisterUserRequest;
import com.divs.homehub.dto.UserResponse;
import com.divs.homehub.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public UserResponse register(@RequestBody RegisterUserRequest request) {
        return userService.register(request);
    }

    @GetMapping
    public List<UserResponse> getAllUsers() {
        return userService.getAllUsers();
    }

}

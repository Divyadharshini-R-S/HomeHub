package com.divs.homehub.service;
import java.util.List;
import com.divs.homehub.dto.RegisterUserRequest;
import com.divs.homehub.dto.UserResponse;


public interface UserService {
    UserResponse register(RegisterUserRequest request);
    List<UserResponse> getAllUsers();
}

package com.divs.homehub.service.impl;
import com.divs.homehub.service.UserService;
import java.util.List;
import com.divs.homehub.dto.RegisterUserRequest;
import com.divs.homehub.dto.UserResponse;
import com.divs.homehub.entity.User;
import com.divs.homehub.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserResponse register(RegisterUserRequest request){
        return null;
    }

    @Override
    public List<UserResponse> getAllUsers() {
        return List.of();
    }

}

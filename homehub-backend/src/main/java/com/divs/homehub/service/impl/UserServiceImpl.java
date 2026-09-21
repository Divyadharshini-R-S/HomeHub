package com.divs.homehub.service.impl;
import com.divs.homehub.service.UserService;
import java.util.List;
import com.divs.homehub.dto.RegisterUserRequest;
import com.divs.homehub.dto.UserResponse;
import com.divs.homehub.entity.User;
import com.divs.homehub.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Service
public class UserServiceImpl implements UserService {
    private final BCryptPasswordEncoder passwordEncoder =
            new BCryptPasswordEncoder();

    private final UserRepository userRepository;
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserResponse register(RegisterUserRequest request) {
        User  user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        //Bcrypt Password saving
        String hashedPassword = passwordEncoder.encode(request.getPassword());
        user.setPassword(hashedPassword);

        User result = userRepository.save(user);
        UserResponse userResponse = new UserResponse();
        userResponse.setId(result.getId());
        userResponse.setUsername(result.getUsername());
        userResponse.setEmail(result.getEmail());
        return userResponse;
    }

    @Override
    public List<UserResponse> getAllUsers() {
        //later during login passwordEncoder.matches(rawPassword, storedHash)
        return List.of();
    }

}

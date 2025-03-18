package com.Oracle.AuthService.service;

import com.Oracle.AuthService.data.UserRegister;
import com.Oracle.AuthService.data.UserResponse;
import com.Oracle.AuthService.model.User;
import com.Oracle.AuthService.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User register(UserRegister userRegister){
        String hashedPassword = passwordEncoder.encode(userRegister.password());
        User user = new User(userRegister);
        user.setPassword(hashedPassword);
        return userRepository.save(user);
    }

    public List<UserResponse> getUsersFiltered(String role, String workMode, Boolean isActive) {
        List<User> users = userRepository.findByFilters(role,workMode,isActive);
        return users.stream()
                .map(user -> new UserResponse(
                        user.getName(),
                        user.getEmail(),
                        user.getRole(),
                        user.getWorkMode()
                ))
                .collect(Collectors.toList());
    }

}

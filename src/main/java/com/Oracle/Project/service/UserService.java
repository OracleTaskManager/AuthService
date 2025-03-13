package com.Oracle.Project.service;

import com.Oracle.Project.data.UserRegister;
import com.Oracle.Project.data.UserResponse;
import com.Oracle.Project.model.User;
import com.Oracle.Project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserResponse register(UserRegister userRegister){
        User user = userRepository.save(new User(userRegister, "Admin"));
        return new UserResponse(user.getName(), user.getEmail(), user.getRole());
    }

}

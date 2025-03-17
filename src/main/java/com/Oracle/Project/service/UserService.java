package com.Oracle.Project.service;

import com.Oracle.Project.data.UserLogin;
import com.Oracle.Project.data.UserRegister;
import com.Oracle.Project.data.UserResponse;
import com.Oracle.Project.model.User;
import com.Oracle.Project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

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

}

package com.Oracle.AuthService.service;

import com.Oracle.AuthService.data.UserRegister;
import com.Oracle.AuthService.data.UserResponse;
import com.Oracle.AuthService.data.UserUpdate;
import com.Oracle.AuthService.data.WorkMode;
import com.Oracle.AuthService.model.User;
import com.Oracle.AuthService.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    public User updateUser(Long user_id, UserUpdate userUpdate){
        User user = userRepository.findById(user_id).get();
        if (userUpdate.name() != null && !userUpdate.name().isEmpty()){
            user.setName(userUpdate.name());
        }
        if (userUpdate.workMode() != null){
            try{
                WorkMode validWorkMode = WorkMode.valueOf(userUpdate.workMode().name());
                user.setWorkMode(validWorkMode.getDisplayName());
            }catch (IllegalArgumentException e){
                throw new IllegalArgumentException("Invalid work mode");
            }
        }
        if (userUpdate.telegramChatId() != null){
            user.setTelegramChatId(userUpdate.telegramChatId());
        }
        userRepository.save(user);
        return user;
    }

    public ResponseEntity<?> deleteUser(Long user_id){
        User user = userRepository.findById(user_id).get();
        userRepository.delete(user);
        return ResponseEntity.ok().build();
    }

    public List<UserResponse> getUsersFiltered(String role, String workMode, Boolean isActive) {
        List<User> users = userRepository.findByFilters(role,workMode,isActive);
        return users.stream()
                .map(user -> new UserResponse(
                        user.getUser_id(),
                        user.getName(),
                        user.getEmail(),
                        user.getRole(),
                        user.getWorkMode()
                ))
                .collect(Collectors.toList());
    }

}

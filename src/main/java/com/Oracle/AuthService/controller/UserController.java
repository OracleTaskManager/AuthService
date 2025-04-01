package com.Oracle.AuthService.controller;

import com.Oracle.AuthService.data.UserLogin;
import com.Oracle.AuthService.data.UserRegister;
import com.Oracle.AuthService.data.UserResponse;
import com.Oracle.AuthService.data.UserUpdate;
import com.Oracle.AuthService.infra.security.DatosJWTToken;
import com.Oracle.AuthService.infra.security.TokenService;
import com.Oracle.AuthService.model.User;
import com.Oracle.AuthService.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid UserRegister userRegister){
        try{
            User user = userService.register(userRegister);
            String jwtToken = tokenService.generateToken(user);
            Map<String, Object> response = Map.of(
                    "token", jwtToken,
                    "user", new UserResponse(user.getUser_id(),user.getName(),user.getEmail(),user.getRole(),user.getWorkMode())
            );
            return ResponseEntity.ok(response);
        }catch (Exception e){
            System.out.println("Error during registration: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid UserLogin userLogin){
        try{
            Authentication requestToken = new UsernamePasswordAuthenticationToken(userLogin.email(), userLogin.password());
            Authentication resultToken = authenticationManager.authenticate(requestToken);
            String JWTtoken = tokenService.generateToken((User) resultToken.getPrincipal());

            return ResponseEntity.ok(new DatosJWTToken(JWTtoken));
        }catch(Exception e){
            System.out.println("Error during login: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/")
    public ResponseEntity<?> updateUser(@RequestBody @Valid UserUpdate userUpdate){
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Long userId = ((User) authentication.getPrincipal()).getUser_id();

            User user = userService.updateUser(userId, userUpdate);
            UserResponse userResponse = new UserResponse(user.getUser_id(),user.getName(),user.getEmail(),user.getRole(),user.getWorkMode());
            return ResponseEntity.ok(userResponse);

        }catch(RuntimeException e){
            System.out.println("Error updating user: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid data provided");
        }
        catch(Exception e){
            System.out.println("Error updating user: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/")
    public ResponseEntity<?> deleteUser(){
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Long userId = ((User) authentication.getPrincipal()).getUser_id();
            userService.deleteUser(userId);
            return ResponseEntity.ok().build();
        }catch(Exception e) {
            System.out.println("Error deleting user: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping
    @PreAuthorize("hasRole('Manager')")
    public ResponseEntity<List<UserResponse>> getUsers(
            @RequestParam(required = false) String role,
            @RequestParam(required = false) String workMode,
            @RequestParam(required = false) Boolean isActive
    ){
        List<UserResponse> users = userService.getUsersFiltered(role,workMode,isActive);
        return ResponseEntity.ok(users);
    }

}

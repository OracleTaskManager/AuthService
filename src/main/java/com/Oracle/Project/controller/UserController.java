package com.Oracle.Project.controller;

import com.Oracle.Project.data.UserLogin;
import com.Oracle.Project.data.UserRegister;
import com.Oracle.Project.data.UserResponse;
import com.Oracle.Project.infra.security.DatosJWTToken;
import com.Oracle.Project.infra.security.TokenService;
import com.Oracle.Project.model.User;
import com.Oracle.Project.service.UserService;
import oracle.jdbc.proxy.annotation.Post;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRegister userRegister){
        User user = userService.register(userRegister);
        String jwtToken = tokenService.generateToken(user);
        Map<String, Object> response = Map.of(
                "token", jwtToken,
                "user", new UserResponse(user.getName(),user.getEmail(),user.getRole(),user.getWorkMode())
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid UserLogin userLogin){
        try{
            Authentication requestToken = new UsernamePasswordAuthenticationToken(userLogin.email(), userLogin.password());
            Authentication resultToken = authenticationManager.authenticate(requestToken);
            String JWTtoken = tokenService.generateToken((User) resultToken.getPrincipal());

            return ResponseEntity.ok(new DatosJWTToken(JWTtoken));
        }catch(Exception e){
            System.out.println("Error during authentication: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

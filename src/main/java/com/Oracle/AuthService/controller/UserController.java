package com.Oracle.AuthService.controller;

import com.Oracle.AuthService.data.*;
import com.Oracle.AuthService.infra.security.DatosJWTToken;
import com.Oracle.AuthService.infra.security.TokenService;
import com.Oracle.AuthService.model.User;
import com.Oracle.AuthService.service.TelegramAuthService;
import com.Oracle.AuthService.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
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
@Tag(name = "User", description = "User operations")
public class UserController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TelegramAuthService telegramAuthService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid UserRegister userRegister){
        try{
            User user = userService.register(userRegister, "Developer");
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


    @PostMapping("/register-admin")
    @PreAuthorize("hasRole('Manager')")
    public ResponseEntity<?> createAdmin(@RequestBody @Valid UserRegister userRegister){
        try{
            User user = userService.register(userRegister, "Manager");
            String jwtToken = tokenService.generateToken(user);
            Map<String, Object> response = Map.of(
                    "token", jwtToken,
                    "user", new UserResponse(user.getUser_id(),user.getName(),user.getEmail(),user.getRole(),user.getWorkMode())
            );
            return ResponseEntity.ok(response);
        }catch (Exception e){
            System.out.println("Error during admin registration: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid UserLogin userLogin){
        try{
            Authentication requestToken = new UsernamePasswordAuthenticationToken(userLogin.email(), userLogin.password());
            Authentication resultToken = authenticationManager.authenticate(requestToken);
            User user = userService.login(userLogin);
            String JWTtoken = tokenService.generateToken((User) resultToken.getPrincipal());

            Map<String, Object> response = Map.of(
                    "token", JWTtoken,
                    "user", new UserResponse(user.getUser_id(),user.getName(),user.getEmail(),user.getRole(),user.getWorkMode())
            );

            return ResponseEntity.ok(response);
        }catch(Exception e){
            System.out.println("Error during login: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/telegram-login")
    public ResponseEntity<?> telegramLogin(
            @RequestHeader("X-Telegram-Bot-Secret") String incomingSecret,
            @RequestBody @Valid TelegramLoginRequest telegramLoginRequest){
        try{
            telegramAuthService.validateRequest(incomingSecret);

            User user = userService.findByTelegramChatId(telegramLoginRequest);
            if(user == null){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
            }
            String jwtToken = tokenService.generateToken(user);

            Map<String, Object> response = Map.of(
                    "jwtToken", jwtToken
            );

            return ResponseEntity.ok(response);
        }catch (Exception e){
            System.out.println("Error during Telegram login: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/link-telegram")
    public ResponseEntity<?> linkTelegramAccount(
            @RequestHeader("Authorization") String token,
            @RequestParam Long chatId
    ){
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Long userId = ((User) authentication.getPrincipal()).getUser_id();

            userService.linkTelegramAccount(userId, chatId);

            return ResponseEntity.ok("Telegram account linked successfully");

        }catch (Exception e){
            System.out.println("Error linking Telegram account: " + e.getMessage());
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

    @GetMapping("/{user_id}")
    public ResponseEntity<?> getUser(@PathVariable Long user_id){
        try{
            User user = userService.getUserById(user_id);
            if(user == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }
            UserResponse userResponse = new UserResponse(user.getUser_id(),user.getName(),user.getEmail(),user.getRole(),user.getWorkMode());
            return ResponseEntity.ok(userResponse);
        }catch(Exception e){
            System.out.println("Error fetching user: " + e.getMessage());
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

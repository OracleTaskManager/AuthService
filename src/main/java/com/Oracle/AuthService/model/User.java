package com.Oracle.AuthService.model;


import com.Oracle.AuthService.data.UserRegister;
import jakarta.persistence.*;
import org.hibernate.annotations.GenerationTime;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @Column(
            name = "user_id",
            columnDefinition = "NUMBER",
            insertable = false,
            updatable = false
    )
    @org.hibernate.annotations.Generated(GenerationTime.INSERT)
    private Long user_id;

    private String name;
    private String email;
    private String password;
    @Column(name = "work_mode")
    private String workMode;
    @Column(name = "telegram_chat_id")
    private Long telegramChatId;
    private String role;
    @Column(name="is_active", columnDefinition = "NUMBER(1) DEFAULT 1")
    private boolean isActive = true;

    public User() {}

    public User(UserRegister userRegister) {
        this.name = userRegister.name();
        this.email = userRegister.email();
        this.password = userRegister.password();
        this.workMode = userRegister.workMode().getDisplayName();
        this.telegramChatId = userRegister.telegramChatId();
        this.role = userRegister.role();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        return List.of(new SimpleGrantedAuthority("ROLE_"+this.role));
    }

    public Long getUser_id() {return user_id;}

    public void setUser_id(Long user_id) {this.user_id = user_id;}

    public String getName() {return name;}

    public void setName(String name) {this.name = name;}

    public String getEmail() {return email;}

    public void setEmail(String email) {this.email = email;}

    public String getPassword() {return password;}

    public String  getWorkMode() { return workMode; }

    public void setWorkMode(String  workMode) { this.workMode = workMode; }

    public String getRole() { return role; }

    public void setRole(String role) { this.role = role; }

    public Long getTelegramChatId() { return telegramChatId; }

    public void setTelegramChatId(Long telegramChatId) { this.telegramChatId = telegramChatId; }

    public boolean isActive() {return isActive;}

    public void setActive(boolean active) {isActive = active;}

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}

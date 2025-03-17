package com.Oracle.Project.model;


import com.Oracle.Project.data.UserRegister;
import com.Oracle.Project.data.Work_Mode;
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
        this.workMode = userRegister.work_mode().getDisplayName();
        this.telegramChatId = userRegister.telegram_chat_id();
        this.role = userRegister.role();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    public Long getUserId() {
        return user_id;
    }

    public void setUserId(Long userId) {
        this.user_id = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public String  getWorkMode() {
        return workMode;
    }

    public void setWorkMode(String  workMode) {
        this.workMode = workMode;
    }
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Long getTelegramChatId() {
        return telegramChatId;
    }

    public void setTelegramChatId(Long telegramChatId) {
        this.telegramChatId = telegramChatId;
    }


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

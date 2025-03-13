package com.Oracle.Project.model;


import com.Oracle.Project.data.UserRegister;
import jakarta.persistence.*;
import org.hibernate.annotations.GenerationTime;

@Entity
@Table(name = "users")
public class User {
    @Id
    @Column(
            name = "user_id",
            columnDefinition = "RAW(16)",
            insertable = false,
            updatable = false
    )
    @org.hibernate.annotations.Generated(GenerationTime.INSERT)
    private byte[] user_id;

    private String name;
    private String email;
    private String password;
    private String role;

    public User() {}

    public User(UserRegister userRegister, String role) {
        this.name = userRegister.name();
        this.email = userRegister.email();
        this.password = userRegister.password();
        this.role = role;
    }

    public byte[] getUserId() {
        return user_id;
    }

    public void setUserId(byte[] userId) {
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

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}

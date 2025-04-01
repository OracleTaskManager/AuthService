package com.Oracle.AuthService.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "teams")
public class Team {
    @Id
    @Column(
            name = "team_id",
            columnDefinition = "NUMBER",
            insertable = false,
            updatable = false
    )
    @org.hibernate.annotations.Generated(org.hibernate.annotations.GenerationTime.INSERT)
    private Long team_id;
    private String team_name;
    private Date created_at;

    public Team() {}

    public Team(String team_name) {
        this.team_name = team_name;
        this.created_at = new Date();
    }

    public Long getTeam_id() {
        return team_id;
    }

    public void setTeam_id(Long team_id) {
        this.team_id = team_id;
    }

    public String getTeam_name() {
        return team_name;
    }

    public void setTeam_name(String team_name) {
        this.team_name = team_name;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }
}

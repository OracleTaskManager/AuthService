package com.Oracle.AuthService.model;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class UserTeamId implements Serializable {

    private Long user_id;
    private Long team_id;

    public UserTeamId() {}

    public UserTeamId(Long user_id, Long team_id) {
        this.user_id = user_id;
        this.team_id = team_id;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public Long getTeam_id() {
        return team_id;
    }

    public void setTeam_id(Long team_id) {
        this.team_id = team_id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(user_id, team_id);
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(obj == null || getClass() != obj.getClass()) return false;
        UserTeamId that = (UserTeamId) obj;
        return Objects.equals(user_id, that.user_id) && Objects.equals(team_id, that.team_id);
    }
}

package com.Oracle.AuthService.model;

import com.Oracle.AuthService.data.UserTeamRegister;
import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "user_teams")
public class UserTeam{

    @EmbeddedId
    private UserTeamId id;

    public UserTeam() {}

    public UserTeam(UserTeamRegister userTeamRegister) {
        this.id = new UserTeamId(userTeamRegister.userId(), userTeamRegister.teamId());
    }

    public UserTeamId getId() {
        return id;
    }

    public void setId(UserTeamId id) {
        this.id = id;
    }

    public Long getUser_id() {
        return id.getUser_id();
    }

    public void setUser_id(Long user_id) {
        this.id.setUser_id(user_id);
    }

    public Long getTeam_id() {
        return id.getTeam_id();
    }

    public void setTeam_id(Long team_id) {
        this.id.setTeam_id(team_id);
    }


}

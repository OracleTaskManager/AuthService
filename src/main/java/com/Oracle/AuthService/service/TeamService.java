package com.Oracle.AuthService.service;

import com.Oracle.AuthService.data.TeamRegister;
import com.Oracle.AuthService.data.TeamUpdate;
import com.Oracle.AuthService.model.Team;
import com.Oracle.AuthService.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TeamService {

    @Autowired
    private TeamRepository teamRepository;

    public Team createTeam(TeamRegister teamRegister){
        Team team = new Team(teamRegister.teamName());
        return teamRepository.save(team);
    }

    public Team updateTeam(TeamUpdate teamUpdate){
        Team team = teamRepository.findById(teamUpdate.teamId()).get();
        team.setTeam_name(teamUpdate.teamName());
        teamRepository.save(team);
        return team;
    }

    public void deleteTeam(Long team_id){
        Team team = teamRepository.findById(team_id).get();
        teamRepository.delete(team);
    }

}

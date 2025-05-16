package com.Oracle.AuthService.service;

import com.Oracle.AuthService.data.TeamResponse;
import com.Oracle.AuthService.data.UserTeamRegister;
import com.Oracle.AuthService.model.Team;
import com.Oracle.AuthService.model.User;
import com.Oracle.AuthService.model.UserTeam;
import com.Oracle.AuthService.repository.TeamRepository;
import com.Oracle.AuthService.repository.UserTeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserTeamService {

    @Autowired
    private UserTeamRepository userTeamRepository;

    @Autowired
    private TeamRepository teamRepository;

    public Object addUserToTeam(UserTeamRegister userTeamRegister){
        UserTeam userTeam = new UserTeam(userTeamRegister);
        return userTeamRepository.save(userTeam);
    }

    public void removeUserFromTeam(UserTeamRegister userTeamRegister){
        UserTeam userTeam = new UserTeam(userTeamRegister);
        userTeamRepository.delete(userTeam);
    }

    public List<UserTeam> findByTeamId(Long team_id){
        return userTeamRepository.findByTeamId(team_id);
    }

    public List<UserTeam> findAll(){
        return userTeamRepository.findAll();
    }

    public void removeAllUsersFromTeam(Long team_id){
        userTeamRepository.deleteAllByTeamId(team_id);
    }

    public boolean isMemberOfTeam(Long user_id, Long team_id){
        return userTeamRepository.existsByUserIdAndTeamId(user_id, team_id);
    }

    public List<TeamResponse> getTeamsByUserId(Long userId) {
        // Obtener IDs de los equipos del usuario
        List<Long> teamIds = userTeamRepository.findTeamIdsByUserId(userId);

        // Obtener información detallada de cada equipo
        List<Team> teams = teamRepository.findAllById(teamIds);

        // Convertir a DTOs para la respuesta
        return teams.stream()
                .map(team -> new TeamResponse(
                        team.getTeam_id(),
                        team.getTeam_name(),
                        team.getCreated_at()
                ))
                .collect(Collectors.toList());
    }
}


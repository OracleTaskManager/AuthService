package com.Oracle.AuthService.service;

import com.Oracle.AuthService.data.UserTeamRegister;
import com.Oracle.AuthService.model.UserTeam;
import com.Oracle.AuthService.repository.UserTeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserTeamService {

    @Autowired
    private UserTeamRepository userTeamRepository;

    public Object addUserToTeam(UserTeamRegister userTeamRegister){
        UserTeam userTeam = new UserTeam(userTeamRegister);
        return userTeamRepository.save(userTeam);
    }

    public void removeUserFromTeam(UserTeamRegister userTeamRegister){
        UserTeam userTeam = new UserTeam(userTeamRegister);
        userTeamRepository.delete(userTeam);
    }

    public void removeAllUsersFromTeam(Long team_id){
        userTeamRepository.deleteAllByTeamId(team_id);
    }

    public boolean isMemberOfTeam(Long user_id, Long team_id){
        return userTeamRepository.existsByUserIdAndTeamId(user_id, team_id);
    }


}

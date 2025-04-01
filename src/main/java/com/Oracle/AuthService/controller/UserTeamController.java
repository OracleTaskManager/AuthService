package com.Oracle.AuthService.controller;

import com.Oracle.AuthService.data.UserTeamRegister;
import com.Oracle.AuthService.service.UserTeamService;
import jakarta.validation.Valid;
import oracle.ucp.proxy.annotation.Pre;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/userteams")
public class UserTeamController {

    @Autowired
    private UserTeamService userTeamService;

    @PostMapping("/add")
    @PreAuthorize("hasRole('Manager')")
    public void addUserToTeam(@RequestBody @Valid UserTeamRegister userTeamRegister){
        try{
            boolean isMember = userTeamService.isMemberOfTeam(userTeamRegister.userId(), userTeamRegister.teamId());
            if(isMember){
                System.out.println("User is already a member of this team");
                return;
            }

            System.out.println("Adding user("+userTeamRegister.userId()+") to team("+userTeamRegister.teamId()+")");

            userTeamService.addUserToTeam(userTeamRegister);
        }catch (Exception e){
            System.out.println("Error during user team creation: " + e.getMessage());
        }
    }

    @DeleteMapping("/remove")
    @PreAuthorize("hasRole('Manager')")
    public void removeUserFromTeam(@RequestBody @Valid UserTeamRegister userTeamRegister){
        try{
            boolean isMember = userTeamService.isMemberOfTeam(userTeamRegister.userId(), userTeamRegister.teamId());
            if(!isMember){
                System.out.println("User is not a member of this team");
                return;
            }
            userTeamService.removeUserFromTeam(userTeamRegister);
        }catch (Exception e){
            System.out.println("Error during user team deletion: " + e.getMessage());
        }
    }

}

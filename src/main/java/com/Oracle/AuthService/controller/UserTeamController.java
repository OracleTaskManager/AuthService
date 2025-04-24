package com.Oracle.AuthService.controller;

import com.Oracle.AuthService.data.UserTeamIdResponse;
import com.Oracle.AuthService.data.UserTeamRegister;
import com.Oracle.AuthService.model.UserTeam;
import com.Oracle.AuthService.model.UserTeamId;
import com.Oracle.AuthService.service.UserTeamService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

            userTeamService.addUserToTeam(userTeamRegister);
        }catch (Exception e){
            System.out.println("Error during user team creation: " + e.getMessage());
        }
    }

    @GetMapping("/")
    public ResponseEntity<?> getAllUserTeams() {
        try{
            List<UserTeam> userTeams = userTeamService.findAll();
            List<UserTeamIdResponse> userTeamIds = userTeams.stream()
                    .map(userTeam -> new UserTeamIdResponse(userTeam.getUser_id(), userTeam.getTeam_id()))
                    .toList();
            return ResponseEntity.ok(userTeamIds);
        } catch (Exception e){
            System.out.println("Error during user teams fetch: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
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

package com.Oracle.AuthService.controller;

import com.Oracle.AuthService.data.TeamResponse;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('Manager')")
    public ResponseEntity<List<Map<String, Object>>> getTeamsByUserId(@PathVariable Long userId) {
        List<TeamResponse> teams = userTeamService.getTeamsByUserId(userId);

        // Convertir a formato Map para compatibilidad con el cliente Feign
        List<Map<String, Object>> response = teams.stream()
                .map(team -> {
                    Map<String, Object> teamMap = new HashMap<>();
                    teamMap.put("team_id", team.team_id());
                    teamMap.put("team_name", team.team_name());
                    teamMap.put("created_at", team.created_at());
                    return teamMap;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

}

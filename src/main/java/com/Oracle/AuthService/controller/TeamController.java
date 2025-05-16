package com.Oracle.AuthService.controller;

import com.Oracle.AuthService.data.*;
import com.Oracle.AuthService.model.Team;
import com.Oracle.AuthService.model.User;
import com.Oracle.AuthService.model.UserTeam;
import com.Oracle.AuthService.service.TeamService;
import com.Oracle.AuthService.service.UserService;
import com.Oracle.AuthService.service.UserTeamService;
import jakarta.validation.Valid;
import oracle.ucp.proxy.annotation.Pre;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/teams")
public class TeamController {

    @Autowired
    private TeamService teamService;

    @Autowired
    private UserTeamService userTeamService;

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('Manager')")
    public ResponseEntity<?> createTeam(@RequestBody @Valid TeamRegister teamRegister){
        try{

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Long userId = ((User) authentication.getPrincipal()).getUser_id();

            Team team = teamService.createTeam(teamRegister);
            UserTeamRegister userTeamRegister = new UserTeamRegister(userId, team.getTeam_id());
            userTeamService.addUserToTeam(userTeamRegister);
            TeamResponse teamResponse = new TeamResponse(team.getTeam_id(),team.getTeam_name(),team.getCreated_at());
            return ResponseEntity.ok(teamResponse);
        }catch(Exception e){
            System.out.println("Error during team creation: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/")
    @PreAuthorize("hasRole('Manager')")
    public ResponseEntity<?> getAllTeams(){
        try{
            List<Team> teams = teamService.getAllTeams();
            List<TeamResponse> teamResponses = teams.stream()
                    .map(team -> new TeamResponse(team.getTeam_id(),team.getTeam_name(),team.getCreated_at()))
                    .toList();
            return ResponseEntity.ok(teamResponses);
        }catch(Exception e){
            System.out.println("Error during fetching teams: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{teamId}")
    @PreAuthorize("hasRole('Manager')")
    public ResponseEntity<?> getTeamById(@PathVariable Long teamId){
        try{
            System.out.println("Team Id: " + teamId);
            Team team = teamService.getTeamById(teamId);
            if(team == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Team not found");
            }
            TeamResponse teamResponse = new TeamResponse(team.getTeam_id(),team.getTeam_name(),team.getCreated_at());
            return ResponseEntity.ok(teamResponse);
        }catch(Exception e){
            System.out.println("Error during fetching team by ID: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{teamId}/users")
    @PreAuthorize("hasRole('Manager')")
    public ResponseEntity<?> getUsersByTeamId(@PathVariable Long teamId){
        try{
            List<UserTeam> userTeams = userTeamService.findByTeamId(teamId);
            List<User> users = userTeams.stream()
                    .map(userTeam -> userService.getUserById(userTeam.getUser_id()))
                    .toList();
            List<UserResponse> userResponses = users.stream()
                    .map(user -> new UserResponse(user.getUser_id(),user.getName(),user.getEmail(), user.getRole(), user.getWorkMode()))
                    .toList();
            return ResponseEntity.ok(userResponses);
        }catch(Exception e){
            System.out.println("Error during fetching users by team ID: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/myteams")
    public ResponseEntity<?> getMyTeams(){
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Long userId = ((User) authentication.getPrincipal()).getUser_id();

            List<Team> teams = teamService.getMyTeams(userId);
            List<TeamResponse> teamResponses = teams.stream()
                    .map(team -> new TeamResponse(team.getTeam_id(),team.getTeam_name(),team.getCreated_at()))
                    .toList();
            return ResponseEntity.ok(teamResponses);
        }catch (Exception e){
            System.out.println("Error during fetching my teams: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('Manager')")
    public ResponseEntity<?> updateTeam(@RequestBody @Valid TeamUpdate teamUpdate){
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Long userId = ((User) authentication.getPrincipal()).getUser_id();

            boolean isMember = userTeamService.isMemberOfTeam(userId, teamUpdate.teamId());

            if(!isMember){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not a member of this team");
            }

            Team team = teamService.updateTeam(teamUpdate);
            TeamResponse teamResponse = new TeamResponse(team.getTeam_id(),team.getTeam_name(),team.getCreated_at());
            return ResponseEntity.ok(teamResponse);
        }catch(Exception e){
            System.out.println("Error during team update: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/")
    @PreAuthorize("hasRole('Manager')")
    public ResponseEntity<?> deleteTeam(@RequestParam Long teamId){
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Long userId = ((User) authentication.getPrincipal()).getUser_id();

            boolean isMember = userTeamService.isMemberOfTeam(userId, teamId);
            if(!isMember){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not a member of this team");
            }
            System.out.println("Deleting team: " + teamId);
            userTeamService.removeAllUsersFromTeam(teamId);
            System.out.println("Users removed from team: " + teamId);
            teamService.deleteTeam(teamId);
            return ResponseEntity.ok().build();
        }catch(Exception e){
            System.out.println("Error during team deletion: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/batch")
    public ResponseEntity<List<Map<String, Object>>> getTeamsByIds(@RequestParam List<Long> ids) {
        List<Team> teams = teamService.getTeamsByIds(ids);

        // Convertir a formato Map para la respuesta
        List<Map<String, Object>> response = convertTeamsToMapList(teams);

        return ResponseEntity.ok(response);
    }

    private List<Map<String, Object>> convertTeamsToMapList(List<Team> teams) {
        return teams.stream()
                .map(team -> {
                    Map<String, Object> teamMap = new HashMap<>();
                    teamMap.put("team_id", team.getTeam_id());
                    teamMap.put("team_name", team.getTeam_name());
                    teamMap.put("created_at", team.getCreated_at());
                    return teamMap;
                })
                .collect(Collectors.toList());
    }

}

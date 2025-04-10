package com.Oracle.AuthService.repository;

import com.Oracle.AuthService.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

    @Query("SELECT t FROM Team t WHERE t.team_id IN (SELECT ut.id.team_id FROM UserTeam ut WHERE ut.id.user_id = ?1)")
    List<Team> findTeamByUserId(Long user_id);

}

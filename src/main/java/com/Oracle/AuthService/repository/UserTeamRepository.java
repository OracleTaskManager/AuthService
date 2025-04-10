package com.Oracle.AuthService.repository;

import com.Oracle.AuthService.model.Team;
import com.Oracle.AuthService.model.UserTeam;
import com.Oracle.AuthService.model.UserTeamId;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserTeamRepository extends JpaRepository<UserTeam, UserTeamId> {

    @Modifying
    @Transactional
    @Query("DELETE FROM UserTeam ut WHERE ut.id.team_id = ?1")
    void deleteAllByTeamId(Long team_id);

    @Query("SELECT CASE WHEN COUNT(ut) > 0 THEN TRUE ELSE FALSE END FROM UserTeam ut WHERE ut.id.user_id = ?1 AND ut.id.team_id = ?2")
    boolean existsByUserIdAndTeamId(Long user_id, Long team_id);

}

package com.Oracle.AuthService.repository;

import com.Oracle.AuthService.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    UserDetails findByEmail(String subject);

    @Query("""
        SELECT u
        FROM User u
        WHERE (:role IS NULL OR u.role = :role)
          AND (:workMode IS NULL OR u.workMode = :workMode)
          AND (:isActive IS NULL OR u.isActive = :isActive)
    """)
    List<User> findByFilters(@Param("role") String role,
                             @Param("workMode") String workMode,
                             @Param("isActive") Boolean isActive);

    User findByTelegramChatId(Long l);

    boolean existsByTelegramChatId(Long telegramChatId);
}
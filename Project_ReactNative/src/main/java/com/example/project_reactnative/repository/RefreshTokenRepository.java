package com.example.project_reactnative.repository;

import com.example.project_reactnative.model.entity.RefreshToken;
import com.example.project_reactnative.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByUser(User user);
    Optional<RefreshToken> findByToken(String token);
    void deleteByUser(User user);
}

package com.example.usersmanagementsystem.repository;

import com.example.usersmanagementsystem.entity.OurUsers;
import com.example.usersmanagementsystem.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Integer> {
    Optional<RefreshToken> findByToken(String token);
    boolean existsByToken(String token);
    @Modifying
    @Transactional
    void deleteByToken(String token);
}

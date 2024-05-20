package com.example.usersmanagementsystem.service;

import com.example.usersmanagementsystem.entity.OurUsers;
import com.example.usersmanagementsystem.entity.RefreshToken;
import com.example.usersmanagementsystem.repository.RefreshTokenRepository;
import com.example.usersmanagementsystem.repository.UsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private UsersRepo usersRepo;

    public RefreshToken createRefreshToken(String username){
        RefreshToken refreshToken = RefreshToken.builder()
                .ourUsers(usersRepo.findByEmail(username).get())
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(600000))
                .build();
        return refreshTokenRepository.save(refreshToken);
    }

    public Optional<RefreshToken> findByToken(String token){
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken verifyExpriration(RefreshToken token){
        if (token.getExpiryDate().compareTo(Instant.now()) < 0){
            refreshTokenRepository.delete(token);
            throw new RuntimeException(token.getToken()+ " Refresh token was expired. Please make a new signin request");

        }
        return token;
    }
public boolean existsByToken(String token){
        return refreshTokenRepository.existsByToken(token);
}
    public void deleteByToken(String token){
         refreshTokenRepository.deleteByToken(token);
    }

}

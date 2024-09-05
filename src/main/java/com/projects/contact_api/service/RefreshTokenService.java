package com.projects.contact_api.service;

import com.projects.contact_api.model.RefreshToken;
import com.projects.contact_api.dao.RefreshTokenDAO;
import com.projects.contact_api.model.User;
import com.projects.contact_api.dao.UserDAO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Log
public class RefreshTokenService {

    private final RefreshTokenDAO refreshTokenDAO;
    private final UserDAO userDAO;

    public RefreshToken createRefreshToken(String username){
        RefreshToken refreshToken = RefreshToken.builder()
                .user(userDAO.findByEmail(username).orElseThrow())
                .token(UUID.randomUUID().toString())
                .expirationDate(Instant.now().plusMillis(1000L * 60 * 60 * 24 * 30)) // 30 Tage (nicht eingeloggt)
                .build();
        return refreshTokenDAO.save(refreshToken);
    }

    public Optional<RefreshToken> findByToken(String token){
        return refreshTokenDAO.findByToken(token);
    }

    public Optional<RefreshToken> findByUser(User user){
        return refreshTokenDAO.findByUser(user);
    }

    public void deleteToken(RefreshToken refreshToken){
        refreshTokenDAO.delete(refreshToken);
    }

    public RefreshToken verifyExpiration(RefreshToken token){
        if(token.getExpirationDate().compareTo(Instant.now())<0){
            refreshTokenDAO.delete(token);
            throw new RuntimeException(token.getToken() + " Refresh token is expired. Please make a new login..!");
        }
        return token;
    }

    @Scheduled(fixedDelay = 1, timeUnit = TimeUnit.DAYS)
    @Transactional
    public void deleteExpiredTokens() {
        refreshTokenDAO.deleteByExpirationDateBefore(Instant.now());
        log.info("Deleted Expired Tokens");
    }
}

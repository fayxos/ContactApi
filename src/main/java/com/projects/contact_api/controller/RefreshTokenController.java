package com.projects.contact_api.controller;

import com.projects.contact_api.dto.response.RefreshTokenResponseDTO;
import com.projects.contact_api.service.JwtService;
import com.projects.contact_api.dto.request.RefreshTokenRequestDTO;
import com.projects.contact_api.model.RefreshToken;
import com.projects.contact_api.service.RefreshTokenService;
import com.projects.contact_api.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/refreshToken")
@RequiredArgsConstructor
public class RefreshTokenController {

    private final RefreshTokenService refreshTokenService;
    private final JwtService jwtService;

    @PostMapping
    public RefreshTokenResponseDTO refreshToken(@RequestBody RefreshTokenRequestDTO refreshTokenRequestDTO){
        return refreshTokenService.findByToken(refreshTokenRequestDTO.getToken())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String accessToken = jwtService.generateToken(((User) user).getEmail());

                    // delete olf refresh token
                    refreshTokenService.deleteToken(refreshTokenService.findByToken(refreshTokenRequestDTO.getToken()).orElseThrow());

                    // generate new refresh token
                    var newRefreshToken = refreshTokenService.createRefreshToken(user.getEmail());

                    return RefreshTokenResponseDTO.builder()
                            .accessToken(accessToken)
                            .refreshToken(newRefreshToken.getToken()).build();
                }).orElseThrow(() ->new RuntimeException("Refresh Token is not in DB..!!"));
    }

}


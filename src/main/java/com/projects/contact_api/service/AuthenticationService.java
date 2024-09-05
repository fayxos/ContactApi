package com.projects.contact_api.service;

import com.projects.contact_api.dto.response.AuthResponseDTO;
import com.projects.contact_api.dto.request.LoginRequestDTO;
import com.projects.contact_api.dto.request.RegisterRequestDTO;
import com.projects.contact_api.model.User;
import com.projects.contact_api.dao.UserDAO;
import com.projects.contact_api.model.UserRole;
import com.projects.contact_api.dao.UserRoleDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserDAO userDAO;
    private final UserRoleDAO userRoleDAO;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;

    public AuthResponseDTO register(RegisterRequestDTO request) {
        if(!userRoleDAO.findByName("USER").isPresent()) userRoleDAO.save(new UserRole("USER"));

        if(userDAO.findByEmail(request.getEmail()).isPresent()) throw new RuntimeException("Email already exists");

        var user = User.builder()
            .firstname(request.getFirstname())
            .lastname(request.getLastname())
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            .roles(Set.of(userRoleDAO.findByName("USER").orElseThrow()))
            .build();

        userDAO.save(user);

        var jwtToken = jwtService.generateToken(user.getUsername());
        var refreshToken = refreshTokenService.createRefreshToken(user.getUsername());

        return AuthResponseDTO.builder()
                .userDetails(user)
                .accessToken(jwtToken)
                .refreshToken(refreshToken.getToken())
                .build();
    }

    public AuthResponseDTO login(LoginRequestDTO request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var user = userDAO.findByEmail(request.getEmail());

        var oldToken = refreshTokenService.findByUser(user.orElseThrow());
        oldToken.ifPresent(refreshTokenService::deleteToken);

        var refreshToken = refreshTokenService.createRefreshToken(request.getEmail());

        var jwtToken = jwtService.generateToken(request.getEmail());
        return AuthResponseDTO.builder()
                .userDetails(user.orElseThrow())
                .accessToken(jwtToken)
                .refreshToken(refreshToken.getToken())
                .build();
    }
}

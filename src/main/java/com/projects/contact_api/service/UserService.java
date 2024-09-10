package com.projects.contact_api.service;

import com.projects.contact_api.dao.UserDAO;
import com.projects.contact_api.model.RefreshToken;
import com.projects.contact_api.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserDAO userDAO;
    private final RefreshTokenService refreshTokenService;

    public List<User> getAllUsers() {
        return userDAO.findAll();
    }

    public User getUserById(Integer userId) {
        return userDAO.findById(userId).orElseThrow();
    }

    public User getAuthenticated() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public User updateUser(Integer userId, User userDetails) {
        User user = getUserById(userId);
        user.updateData(userDetails);
        return userDAO.save(user);
    }

    public Boolean isEmailVerified(Integer userId) {
        User user = getUserById(userId);
        return user.isEmailVerified();
    }

    public void verifyEmail(Integer userId) {
        User user = getUserById(userId);
        user.setEmailVerified(!user.isEmailVerified());
        userDAO.save(user);
    }

    public void deleteUser(Integer userId) {
        User user = userDAO.findById(userId).orElseThrow();
        RefreshToken refreshToken = refreshTokenService.findByUser(user).orElseThrow();
        refreshTokenService.deleteToken(refreshToken);
        SecurityContextHolder.clearContext();
        userDAO.delete(user);
    }

    public void logout(Integer userId) {
        User user = userDAO.findById(userId).orElseThrow();
        RefreshToken refreshToken = refreshTokenService.findByUser(user).orElseThrow();
        refreshTokenService.deleteToken(refreshToken);
        SecurityContextHolder.clearContext();
    }
}

package com.projects.contact_api.controller;

import com.projects.contact_api.dto.response.EmailVerifiedResponseDTO;
import com.projects.contact_api.model.User;
import com.projects.contact_api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PreAuthorize(value = "hasAuthority('ADMIN')")
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable Integer userId) {
        User user = userService.getUserById(userId);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/getAuthenticated")
    public ResponseEntity<User> getAuthenticated() {
        User user = userService.getAuthenticated();
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable Integer userId, @RequestBody User userDetails) {
        User updatedUser = userService.updateUser(userId, userDetails);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @GetMapping("/{userId}/isEmailVerified")
    public ResponseEntity<EmailVerifiedResponseDTO> isEmailVerified(@PathVariable Integer userId) {
        Boolean isEmailVerified = userService.isEmailVerified(userId);
        EmailVerifiedResponseDTO responseDTO = EmailVerifiedResponseDTO.builder().isEmailVerified(isEmailVerified).build();
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    // TODO verify Email and return link that opens app
    @PostMapping("/{userId}/verifyEmail")
    public ResponseEntity<Boolean> verifyEmail(@PathVariable Integer userId) {
        userService.verifyEmail(userId);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer userId) {
        userService.deleteUser(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{userId}/logout")
    public ResponseEntity<Void> logout(@PathVariable Integer userId) {
        userService.logout(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}

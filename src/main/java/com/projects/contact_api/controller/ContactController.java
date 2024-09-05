package com.projects.contact_api.controller;

import com.projects.contact_api.dto.request.UploadImageRequestDTO;
import com.projects.contact_api.model.ContactConfig;
import com.projects.contact_api.model.ContactInfo;
import com.projects.contact_api.model.User;
import com.projects.contact_api.service.ContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class ContactController {

    private final ContactService contactService;

    // ContactInfo

    @GetMapping("/{userId}/info")
    public ResponseEntity<ContactInfo> getInfoByUserId(@PathVariable Integer userId) {
        ContactInfo info = contactService.getInfoByUserId(userId);
        return new ResponseEntity<>(info, HttpStatus.OK);
    }

    @GetMapping(value = "/{userId}/images/{imageName}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<InputStreamResource> getImage(@PathVariable Integer userId, @PathVariable String imageName) throws IOException {
        InputStreamResource image = contactService.getImage(userId, imageName);
        return new ResponseEntity<>(image,  HttpStatus.OK);
    }

    @PostMapping("/{userId}/info")
    public ResponseEntity<ContactInfo> createInfo(@PathVariable Integer userId, @RequestBody ContactInfo info) {
        ContactInfo newInfo = contactService.createInfo(userId, info);
        return new ResponseEntity<>(newInfo, HttpStatus.CREATED);
    }

    @PostMapping(value = "/{userId}/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ContactInfo> uploadImage(@PathVariable Integer userId, @RequestPart("image") MultipartFile multipartFile) throws IOException {
        ContactInfo newInfo = contactService.uploadImage(userId, multipartFile);
        return new ResponseEntity<>(newInfo, HttpStatus.CREATED);
    }

    @PutMapping("/{userId}/info")
    public ResponseEntity<ContactInfo> updateInfo(@PathVariable Integer userId, @RequestBody ContactInfo infoData) {
        ContactInfo info = contactService.updateInfo(userId, infoData);
        return new ResponseEntity<>(info, HttpStatus.OK);
    }

    // Get Info From Connection With Config
    @GetMapping("/{userId}/{connectionId}")
    public ResponseEntity<ContactInfo> getInfoFromConnection(@PathVariable Integer userId, @PathVariable Integer connectionId) throws IllegalAccessException {
        ContactInfo info = contactService.getInfoFromConnection(userId, connectionId);
        return new ResponseEntity<>(info, HttpStatus.OK);
    }

    @GetMapping(value = "/{userId}/{connectionId}/image", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<InputStreamResource> getImageFromConnection(@PathVariable Integer userId, @PathVariable Integer connectionId) throws IOException {
        InputStreamResource image = contactService.getImageFromConnection(userId, connectionId);
        return new ResponseEntity<>(image, HttpStatus.OK);
    }

    // ContactConfig

    @GetMapping("/{userId}/configs")
    public List<ContactConfig> getConfigsByUserId(@PathVariable Integer userId) {
        return contactService.getConfigsByUserId(userId);
    }

    @GetMapping("/{userId}/configs/{configId}")
    public ResponseEntity<ContactConfig> getConfigById(@PathVariable Integer configId) {
        ContactConfig config = contactService.getConfigById(configId);
        return new ResponseEntity<>(config, HttpStatus.OK);
    }

    @PostMapping("/{userId}/configs")
    public ResponseEntity<ContactConfig> createConfig(@PathVariable Integer userId, @RequestBody ContactConfig config) {
        ContactConfig newConfig = contactService.createConfig(userId, config);
        return new ResponseEntity<>(newConfig, HttpStatus.CREATED);
    }

    @PutMapping("/{userId}/configs/{configId}")
    public ResponseEntity<ContactConfig> updateConfig(@PathVariable Integer configId, @RequestBody ContactConfig config) {
        ContactConfig updatedConfig = contactService.updateConfig(configId, config);
        return new ResponseEntity<>(updatedConfig, HttpStatus.OK);
    }

    @DeleteMapping("/{userId}/configs/{configId}")
    public ResponseEntity<Void> deleteConfig(@PathVariable Integer configId) {
        contactService.deleteConfig(configId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

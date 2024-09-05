package com.projects.contact_api.controller;

import com.projects.contact_api.dto.request.ConnectionLinkRequestDTO;
import com.projects.contact_api.model.Connection;
import com.projects.contact_api.model.ConnectionLink;
import com.projects.contact_api.model.ContactConfig;
import com.projects.contact_api.service.ConnectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class ConnectionController {

    private final ConnectionService connectionService;

    // TODO per app-site-association file app öffnen wenn "/connect/{link}" aufgerufen wird

    @GetMapping("/{userId}/connections")
    public List<Connection> getConnectionsByUserId(@PathVariable Integer userId) {
        return connectionService.getConnectionsByUserId(userId);
    }

    @GetMapping("/{userId}/connections/{connectionId}")
    public ResponseEntity<Connection> getConnectionById(@PathVariable Integer userId, @PathVariable Integer connectionId) {
        Connection connection = connectionService.getConnectionById(userId, connectionId);
        return new ResponseEntity<>(connection, HttpStatus.OK);
    }

    // Connection erstellen mit Einladungslink
    @PostMapping("/{userId}/connections/{link}")
    public ResponseEntity<Connection> createConnectionByLink(@PathVariable Integer userId, @PathVariable String link, @RequestBody ContactConfig config) {
        Connection newConnection = connectionService.createConnection(userId, link, config);
        return new ResponseEntity<>(newConnection, HttpStatus.CREATED);
    }

    @PutMapping("/{userId}/connections/{connectionId}")
    public ResponseEntity<Connection> updateConnection(@PathVariable Integer userId, @PathVariable Integer connectionId, @RequestBody ContactConfig config) {
        Connection updatedConnection = connectionService.updateConnection(userId, connectionId, config);
        return new ResponseEntity<>(updatedConnection, HttpStatus.OK);
    }

    @DeleteMapping("/{userId}/connections/{connectionId}")
    public ResponseEntity<Void> deleteConnection(@PathVariable Integer userId, @PathVariable Integer connectionId) {
        connectionService.deleteConnection(userId, connectionId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Connection Links

    @GetMapping("/{userId}/connection_links")
    public List<ConnectionLink> getConnectionLinksByUserId(@PathVariable Integer userId) {
        return connectionService.getConnectionLinksByUserId(userId);
    }

    @GetMapping("/{userId}/connection_links/{connectionLinkId}")
    public ResponseEntity<ConnectionLink> getConnectionLinkById(@PathVariable Integer connectionLinkId) {
        ConnectionLink connectionLink = connectionService.getConnectionLinkById(connectionLinkId);
        return new ResponseEntity<>(connectionLink, HttpStatus.OK);
    }

    @PostMapping("/{userId}/connection_links")
    public ResponseEntity<ConnectionLink> createConnectionLink(@PathVariable Integer userId, @RequestBody ConnectionLinkRequestDTO request) {
        ConnectionLink newConnectionLink = connectionService.createConnectionLink(userId, request);
        return new ResponseEntity<>(newConnectionLink, HttpStatus.CREATED);
    }

    @DeleteMapping("/{userId}/connection_links/{connectionLinkId}")
    public ResponseEntity<Void> deleteConnectionLink(@PathVariable Integer connectionLinkId) {
        connectionService.deleteConnectionLink(connectionLinkId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

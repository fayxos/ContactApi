package com.projects.contact_api.service;

import com.projects.contact_api.dao.*;
import com.projects.contact_api.dto.request.ConnectionLinkRequestDTO;
import com.projects.contact_api.helper.LinkGenerator;
import com.projects.contact_api.model.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Log
public class ConnectionService {

    private final ConnectionDAO connectionDAO;
    private final ConnectionLinkDAO connectionLinkDAO;
    private final ContactConfigDAO contactConfigDAO;
    private final UserDAO userDAO;

    public List<Connection> getConnectionsByUserId(Integer userId) {
        User user = userDAO.findById(userId).orElseThrow();
        List<Connection> connections = connectionDAO.findByUser1(user);
        connections.addAll(connectionDAO.findByUser2(user));
        return connections;
    }

    public Connection getConnectionById(Integer userId, Integer connectionId) {
        Connection connection = connectionDAO.findById(connectionId).orElseThrow();

        if(!Objects.equals(connection.getUser1().getId(), userId) && !Objects.equals(connection.getUser2().getId(), userId)) throw new AccessDeniedException("User has no access to this connection");

        return connection;
    }

    public Connection createConnection(Integer userId, String link, ContactConfig config) {
        ConnectionLink connectionLink = connectionLinkDAO.findByLink(link).orElseThrow();

        if(Objects.equals(connectionLink.getUser().getId(), userId)) throw new RuntimeException("Can't connect with oneselfs");

        // check if expired
        if (connectionLink.getExpirationDate() != null && connectionLink.getExpirationDate().isBefore(Instant.now())) throw new RuntimeException("Link already expired");

        User user1 = connectionLink.getUser();
        User user2 = userDAO.findById(userId).orElseThrow();

        // if already existing open contact info
        Optional<Connection> existingConnection1 = connectionDAO.findByUser1AndUser2(user1, user2);
        Optional<Connection> existingConnection2 = connectionDAO.findByUser1AndUser2(user2, user1);
        if (existingConnection1.isPresent()) return existingConnection1.orElseThrow();
        if (existingConnection2.isPresent()) return existingConnection2.orElseThrow();

        if(config.getId() != null && contactConfigDAO.findById(config.getId()).isPresent()) {
            config = contactConfigDAO.findById(config.getId()).orElseThrow();
            if(!Objects.equals(config.getUser().getId(), userId)) throw new AccessDeniedException("This config doesn't belong to the user");
        }
        else if(config.getConfigType() == ConfigType.PERSONAL) {
            config.setUser(user2);
            config = contactConfigDAO.save(config);
        }

        Connection newConnection = Connection.builder()
                .user1(user1)
                .user2(user2)
                .config1(connectionLink.getConfig())
                .config2(config)
                .build();

        if (connectionLink.getExpirationType().equals(ExpirationType.ONETIME)) {
            connectionLinkDAO.delete(connectionLink);
        }

        return connectionDAO.save(newConnection);
    }

    public Connection updateConnection(Integer userId, Integer connectionId, ContactConfig config) {
        Connection connection = connectionDAO.findById(connectionId).orElseThrow();
        User user = userDAO.findById(userId).orElseThrow();

        if(config.getId() != null && contactConfigDAO.findById(config.getId()).isPresent()) {
            config = contactConfigDAO.findById(config.getId()).orElseThrow();
            if(!Objects.equals(config.getUser().getId(), userId)) throw new AccessDeniedException("This config doesn't belong to the user");
        }
        else if(config.getConfigType() == ConfigType.PERSONAL) {
            config.setUser(user);
            config = contactConfigDAO.save(config);
        }

        if(Objects.equals(connection.getUser1().getId(), userId)) connection.setConfig1(config);
        else if(Objects.equals(connection.getUser2().getId(), userId)) connection.setConfig2(config);
        else {
            if(config.getConfigType() == ConfigType.PERSONAL) contactConfigDAO.delete(config);

            throw new AccessDeniedException("User has no access to this connection");
        }

        return connectionDAO.save(connection);
    }

    public void deleteConnection(Integer userId, Integer connectionId) {
        Connection connection = connectionDAO.findById(connectionId).orElseThrow();

        if(!Objects.equals(connection.getUser1().getId(), userId) && !Objects.equals(connection.getUser2().getId(), userId)) throw new AccessDeniedException("User has no access to this connection");

        connectionDAO.delete(connection);
    }

    // Connection Links

    public List<ConnectionLink> getConnectionLinksByUserId(Integer userId) {
        User user = userDAO.findById(userId).orElseThrow();
        return connectionLinkDAO.findByUser(user);
    }

    public ConnectionLink getConnectionLinkById(Integer connectionLinkId) {
        return connectionLinkDAO.findById(connectionLinkId).orElseThrow();
    }

    public ConnectionLink createConnectionLink(Integer userId, ConnectionLinkRequestDTO request) {
        User user = userDAO.findById(userId).orElseThrow();

        Instant expirationDate = null;
        if (request.getExpirationType() == ExpirationType.DAY) {
            expirationDate = Instant.now().plusMillis(1000L * 60 * 60 * 24);
        }
        else if (request.getExpirationType() == ExpirationType.WEEK) {
            expirationDate = Instant.now().plusMillis(1000L * 60 * 60 * 24 * 7);
        }
        if (request.getExpirationType() == ExpirationType.MONTH) {
            expirationDate = Instant.now().plusMillis(1000L * 60 * 60 * 24 * 30);
        }

        ContactConfig config = request.getContactConfig();
        if(config.getId() != null && contactConfigDAO.findById(config.getId()).isPresent()) {
            config = contactConfigDAO.findById(config.getId()).orElseThrow();
            if(!Objects.equals(config.getUser().getId(), userId)) throw new AccessDeniedException("This config doesn't belong to the user");
        }
        else if(config.getConfigType() == ConfigType.PERSONAL) {
            config.setUser(user);
            config = contactConfigDAO.save(config);
        }

        String link;
        do {
            link = LinkGenerator.generateLink();
        } while (connectionLinkDAO.findByLink(link).isPresent());

        ConnectionLink newLink = ConnectionLink.builder()
                .config(config)
                .user(user)
                .expirationType(request.getExpirationType())
                .expirationDate(expirationDate)
                .link(link)
                .build();

        return connectionLinkDAO.save(newLink);
    }

    public void deleteConnectionLink(Integer connectionLinkId) {
        ConnectionLink connectionLink = connectionLinkDAO.findById(connectionLinkId).orElseThrow();
        connectionLinkDAO.delete(connectionLink);
    }

    @Scheduled(fixedDelay = 1, timeUnit = TimeUnit.DAYS)
    @Transactional
    public void deleteExpiredLinks() {
        connectionLinkDAO.deleteByExpirationDateBefore(Instant.now());
        log.info("Deleted Expired Links");
    }
}

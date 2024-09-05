package com.projects.contact_api.dao;

import com.projects.contact_api.model.Connection;
import com.projects.contact_api.model.ConnectionLink;
import com.projects.contact_api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface ConnectionLinkDAO extends JpaRepository<ConnectionLink, Integer> {

    List<ConnectionLink> findByUser(User user);
    Optional<ConnectionLink> findByLink(String link);
    void deleteByExpirationDateBefore(Instant currentInstant);
}

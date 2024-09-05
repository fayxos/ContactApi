package com.projects.contact_api.dao;

import com.projects.contact_api.model.Connection;
import com.projects.contact_api.model.ContactConfig;
import com.projects.contact_api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ConnectionDAO extends JpaRepository<Connection, Integer> {
    List<Connection> findByUser1(User user1);
    List<Connection> findByUser2(User user2);
    Optional<Connection> findByUser1AndUser2(User user1, User user2);
}

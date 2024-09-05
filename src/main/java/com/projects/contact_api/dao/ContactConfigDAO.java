package com.projects.contact_api.dao;

import com.projects.contact_api.model.ContactConfig;
import com.projects.contact_api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContactConfigDAO extends JpaRepository<ContactConfig, Integer> {
    List<ContactConfig> findByUser(User user);
}

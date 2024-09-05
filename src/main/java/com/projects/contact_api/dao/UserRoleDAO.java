package com.projects.contact_api.dao;

import com.projects.contact_api.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRoleDAO extends JpaRepository<UserRole, Integer> {

    Optional<UserRole> findByName(String name);

}

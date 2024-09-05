package com.projects.contact_api.dao;

import com.projects.contact_api.model.ContactInfo;
import com.projects.contact_api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ContactInfoDAO extends JpaRepository<ContactInfo, Integer> {

    //@Query(value = "SELECT * FROM contact_info WHERE user_id = ?1", nativeQuery = true)
    Optional<ContactInfo> findByUser(User user);
}

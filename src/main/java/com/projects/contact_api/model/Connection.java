package com.projects.contact_api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "connection")
public class Connection {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user1_id", referencedColumnName = "id")
    private User user1;

    @ManyToOne
    @JoinColumn(name = "user2_id", referencedColumnName = "id")
    private User user2;

    @JoinColumn(name = "config1_id", referencedColumnName = "id")
    @ManyToOne
    private ContactConfig config1;

    @JoinColumn(name = "config2_id", referencedColumnName = "id")
    @ManyToOne
    private ContactConfig config2;

    public void updateData(Connection connection) {
        if(connection.getConfig1() != null) setConfig1(connection.getConfig1());
        if(connection.getConfig2() != null) setConfig2(connection.getConfig2());
    }
}

package com.projects.contact_api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "connection_link")
public class ConnectionLink {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @JoinColumn(name = "config_id", referencedColumnName = "id")
    @ManyToOne
    private ContactConfig config;

    @Column(unique = true)
    private String link;

    private ExpirationType expirationType;
    private Instant expirationDate;

    public void updateData(ConnectionLink connectionLink) {
        if(connectionLink.getConfig() != null) setConfig(connectionLink.getConfig());
    }
}

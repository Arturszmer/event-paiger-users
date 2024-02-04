package com.eventpaiger.user.model;

import com.eventpaiger.common.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "user_profile")
@Getter
@NoArgsConstructor
public class UserProfile extends BaseEntity<Long> {

    @Column(nullable = false)
    private String username;

    @Column
    private String password;

    @Column(nullable = false)
    private String email;

    @Column(unique = true, name = "event_organizer_id")
    private UUID eventOrganizerId;

    public UserProfile(String username, String email, UUID eventOrganizerId) {
        this.username = username;
        this.email = email;
        this.eventOrganizerId = eventOrganizerId;
    }
}

package com.eventpaiger.authentication;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TokenClaims {

    private Collection<GrantedAuthority> authorities;
    @JsonProperty(value = "sub")
    private String username;
    private String email;
    @JsonProperty(value = "event_organizer_id")
    private UUID eventOrganizerId;
    private String scope;
}

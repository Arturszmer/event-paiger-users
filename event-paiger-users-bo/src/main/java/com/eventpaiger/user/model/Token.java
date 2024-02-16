package com.eventpaiger.user.model;

import com.eventpaiger.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import static com.eventpaiger.user.model.TokenType.BEARER;

@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "token")
public class Token extends BaseEntity<Long> {

    @Column(unique = true)
    public String token;

    @Enumerated(EnumType.STRING)
    public TokenType tokenType = BEARER;

    public boolean revoked;

    public boolean expired;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_profile_id")
    public UserProfile userProfile;

    public Token(UserProfile userProfile, String token) {
        this.token = token;
        this.userProfile = userProfile;
    }

    public static Token generateToken(UserProfile userProfile, String token){
        return new Token(userProfile, token);
    }

    public void terminate() {
        this.expired = true;
        this.revoked = true;
    }
}

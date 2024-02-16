package com.eventpaiger.user.model;

import com.eventpaiger.common.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Stream;

@Entity
@Table(name = "user_profile")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile extends BaseEntity<Long> implements UserDetails {

    @Column(nullable = false, unique = true)
    private String username;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column
    private String password;

    @OneToMany(mappedBy = "userProfile")
    private List<Token> tokens;

    @Column(unique = true, name = "event_organizer_id")
    private UUID eventOrganizerId;

    @Column(name = "roles")
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(name = "user_profile_roles",
                joinColumns = @JoinColumn(name = "user_profile_id", referencedColumnName = "ID"),
                inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "ID"))
    private List<Role> roles = new ArrayList<>();

    private UserProfile(String username, String email, UUID eventOrganizerId) {
        this.username = username;
        this.email = email;
        this.eventOrganizerId = eventOrganizerId;
    }

    private UserProfile(String username,  String email, String password) {
        this.username = username;
        this.password = password;
        this.email = email;
        addRole(new Role(RoleType.OBSERVER));
    }

    public static UserProfile createForEvent(String username,
                                             String email,
                                             UUID eventOrganizerId){
        return new UserProfile(username, email, eventOrganizerId);
    }

    public static UserProfile createForRegistration(String username,
                                                    String email,
                                                    String password){
        return new UserProfile(username, email, password);
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .flatMap(role -> Stream.concat(
                        Stream.of(new SimpleGrantedAuthority(
                                "ROLE_" + role.getName())),
                        role.getPermissions().stream()
                                .map(permission ->
                                        new SimpleGrantedAuthority(permission.getName())))
                )
                .toList();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void addRole(Role role){
       this.roles.add(role);
    }
}

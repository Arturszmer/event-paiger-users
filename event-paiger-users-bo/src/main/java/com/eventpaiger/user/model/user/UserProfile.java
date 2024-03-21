package com.eventpaiger.user.model.user;

import com.eventpaiger.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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

    @Column(unique = true, name = "event_organizer_id")
    private UUID eventOrganizerId;

    @Column(name = "roles")
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(name = "user_profile_roles",
                joinColumns = @JoinColumn(name = "user_profile_id", referencedColumnName = "ID"),
                inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "ID"))
    private List<Role> roles = new ArrayList<>();

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @Setter
    private SimpleAddress userAddress;

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

    public void setNewPassword(String newPassword) {
        password = newPassword;
    }

    public void setEventOrganizerId() {
        this.eventOrganizerId = UUID.randomUUID();
    }
}

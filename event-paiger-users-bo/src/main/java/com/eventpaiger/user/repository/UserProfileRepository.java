package com.eventpaiger.user.repository;

import com.eventpaiger.user.model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

    Optional<UserProfile> findUserProfileByUsername(String username);

    Optional<UserProfile> findUserProfileByEmail(String email);

}

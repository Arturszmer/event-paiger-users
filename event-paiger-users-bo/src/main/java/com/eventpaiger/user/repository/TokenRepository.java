package com.eventpaiger.user.repository;

import com.eventpaiger.user.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {

    @Query("SELECT t from Token t " +
            "inner join UserProfile up " +
            "on t.userProfile.id = up.id " +
            "where up.id = :userId and (t.expired = false or t.revoked = false)")
    List<Token> findAllValidTokens(@Param("userId") long userId);
}

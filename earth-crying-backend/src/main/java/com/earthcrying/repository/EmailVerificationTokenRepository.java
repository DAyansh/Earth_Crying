package com.earthcrying.repository;

import com.earthcrying.entity.EmailVerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmailVerificationTokenRepository extends JpaRepository<EmailVerificationToken, UUID> {

    Optional<EmailVerificationToken> findByTokenHash(String tokenHash);

    Optional<EmailVerificationToken> findByUserId(UUID userId);

    @Modifying
    @Query("UPDATE EmailVerificationToken ev SET ev.used = true WHERE ev.id = :tokenId")
    void markAsUsed(@Param("tokenId") UUID tokenId);

    @Modifying
    @Query("DELETE FROM EmailVerificationToken ev WHERE ev.expiresAt < :expiryDate")
    int deleteExpired(@Param("expiryDate") OffsetDateTime expiryDate);
}
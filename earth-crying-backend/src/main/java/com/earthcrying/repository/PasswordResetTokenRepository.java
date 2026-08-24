package com.earthcrying.repository;

import com.earthcrying.entity.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, UUID> {

    Optional<PasswordResetToken> findByTokenHash(String tokenHash);

    Optional<PasswordResetToken> findByUserId(UUID userId);

    @Modifying
    @Query("UPDATE PasswordResetToken pr SET pr.used = true WHERE pr.id = :tokenId")
    void markAsUsed(@Param("tokenId") UUID tokenId);

    @Modifying
    @Query("DELETE FROM PasswordResetToken pr WHERE pr.expiresAt < :expiryDate")
    int deleteExpired(@Param("expiryDate") OffsetDateTime expiryDate);
}
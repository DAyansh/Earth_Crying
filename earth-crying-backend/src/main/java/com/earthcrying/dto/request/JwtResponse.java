package com.earthcrying.dto.request;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtResponse {

    private String accessToken;
    private String tokenType = "Bearer";
    private Long expiresIn;
    private String refreshToken;
    private UserDTO user;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UserDTO {
        private UUID id;
        private String email;
        private String displayName;
        private String avatarUrl;
        private String role;
    }
}
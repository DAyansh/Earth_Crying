package com.earthcrying.controller;

import com.earthcrying.dto.request.JwtResponse;
import com.earthcrying.dto.request.UserLoginRequest;
import com.earthcrying.dto.request.UserRegistrationRequest;
import com.earthcrying.security.JwtUtils;
import com.earthcrying.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Authentication", description = "User authentication and registration endpoints")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

        private final AuthenticationManager authenticationManager;
        private final UserService userService;
        private final JwtUtils jwtUtils;

        @PostMapping("/register")
        @Operation(summary = "Register a new user")
        public ResponseEntity<JwtResponse> register(@Valid @RequestBody UserRegistrationRequest request) {
                var user = userService.registerUser(request);

                var authentication = authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

                SecurityContextHolder.getContext().setAuthentication(authentication);

                var jwt = jwtUtils.generateJwtToken(authentication);

                var userDTO = JwtResponse.UserDTO.builder()
                                .id(user.getId())
                                .email(user.getEmail())
                                .displayName(user.getDisplayName())
                                .avatarUrl(user.getAvatarUrl())
                                .role(user.getRole())
                                .build();

                var jwtResponse = JwtResponse.builder()
                                .accessToken(jwt)
                                .refreshToken(jwt)
                                .expiresIn((long) jwtUtils.getJwtExpirationMs() / 1000)
                                .user(userDTO)
                                .build();

                return ResponseEntity.ok(jwtResponse);
        }

        @PostMapping("/login")
        @Operation(summary = "Authenticate user and return JWT token")
        public ResponseEntity<JwtResponse> authenticate(@Valid @RequestBody UserLoginRequest request) {
                var authentication = authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

                SecurityContextHolder.getContext().setAuthentication(authentication);

                var jwt = jwtUtils.generateJwtToken(authentication);

                var user = userService.getUserByEmail(request.getEmail());

                var userDTO = JwtResponse.UserDTO.builder()
                                .id(UUID.fromString(user.getId()))
                                .email(user.getEmail())
                                .displayName(user.getDisplayName())
                                .avatarUrl(user.getAvatarUrl())
                                .role(user.getRole())
                                .build();

                var jwtResponse = JwtResponse.builder()
                                .accessToken(jwt)
                                .refreshToken(jwt)
                                .expiresIn((long) jwtUtils.getJwtExpirationMs() / 1000)
                                .user(userDTO)
                                .build();

                return ResponseEntity.ok(jwtResponse);
        }

        @PostMapping("/logout")
        @Operation(summary = "Logout user by invalidating JWT token")
        public ResponseEntity<Void> logout() {
                SecurityContextHolder.clearContext();
                return ResponseEntity.ok().build();
        }
}
package com.earthcrying.controller;

import com.earthcrying.dto.FootprintComparisonDTO;
import com.earthcrying.service.FootprintComparisonService;
import com.earthcrying.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Footprint Comparison", description = "Compare user footprint against country averages and sustainable targets")
@RestController
@RequestMapping("/api/footprint/comparison")
@RequiredArgsConstructor
public class FootprintComparisonController {

    private final FootprintComparisonService comparisonService;
    private final UserService userService;

    @GetMapping
    @Operation(summary = "Get footprint comparison for current user")
    public ResponseEntity<FootprintComparisonDTO> getComparison(
            @RequestParam(required = false) String countryCode,
            HttpServletRequest request) {
        
        String userId = getCurrentUserId();
        if (userId == null) {
            return ResponseEntity.status(401).build();
        }

        FootprintComparisonDTO comparison = comparisonService.compareFootprint(userId, countryCode);
        return ResponseEntity.ok(comparison);
    }

    @GetMapping("/by-session/{sessionId}")
    @Operation(summary = "Get footprint comparison by session ID (no auth required)")
    public ResponseEntity<FootprintComparisonDTO> getComparisonBySession(
            @PathVariable String sessionId,
            @RequestParam(required = false) String countryCode) {
        
        return ResponseEntity.ok(comparisonService.compareFootprintBySession(sessionId, countryCode));
    }

    private String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()
                && !"anonymousUser".equals(authentication.getPrincipal())) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof org.springframework.security.core.userdetails.UserDetails userDetails) {
                try {
                    return userService.getUserEntityByEmail(userDetails.getUsername()).getId().toString();
                } catch (Exception e) {
                    return null;
                }
            }
        }
        return null;
    }
}

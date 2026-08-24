package com.earthcrying.controller;

import com.earthcrying.dto.ConfessionDTO;
import com.earthcrying.dto.request.ConfessionSubmitRequest;
import com.earthcrying.service.ConfessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Confessions", description = "Anonymous confession wall for environmental impact admissions")
@RestController
@RequestMapping("/api/confessions")
@RequiredArgsConstructor
public class ConfessionController {

    private final ConfessionService confessionService;

    @PostMapping
    @Operation(summary = "Submit an anonymous confession")
    public ResponseEntity<ConfessionDTO> submitConfession(
            @Valid @RequestBody ConfessionSubmitRequest request,
            HttpServletRequest httpRequest) {
        
        String ipHash = hashIp(getClientIp(httpRequest));
        String userAgent = httpRequest.getHeader("User-Agent");
        
        ConfessionDTO confession = confessionService.submitConfession(
                request.getContent(),
                request.getImpactCategory(),
                ipHash,
                userAgent
        );
        return ResponseEntity.ok(confession);
    }

    @GetMapping
    @Operation(summary = "Get approved confessions for the admission wall")
    public ResponseEntity<List<ConfessionDTO>> getApprovedConfessions(
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "50") int limit) {
        
        List<ConfessionDTO> confessions;
        if (category != null && !category.isBlank()) {
            try {
                com.earthcrying.entity.ImpactCategory impactCategory = 
                    com.earthcrying.entity.ImpactCategory.valueOf(category.toUpperCase());
                confessions = confessionService.getApprovedConfessionsByCategory(impactCategory, limit);
            } catch (IllegalArgumentException e) {
                confessions = confessionService.getApprovedConfessions(limit);
            }
        } else {
            confessions = confessionService.getApprovedConfessions(limit);
        }
        return ResponseEntity.ok(confessions);
    }

    @GetMapping("/count")
    @Operation(summary = "Get count of approved confessions")
    public ResponseEntity<Long> getApprovedCount() {
        return ResponseEntity.ok(confessionService.getApprovedCount());
    }

    private String getClientIp(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader != null && !xfHeader.isBlank()) {
            return xfHeader.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }

    private String hashIp(String ip) {
        try {
            java.security.MessageDigest digest = java.security.MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(ip.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            return ip;
        }
    }
}
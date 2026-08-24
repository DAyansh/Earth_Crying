package com.earthcrying.controller;

import com.earthcrying.dto.RippleDTO;
import com.earthcrying.service.RippleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Ripple", description = "Real-time aggregate counts of users completing the same actions")
@RestController
@RequestMapping("/api/ripple")
@RequiredArgsConstructor
public class RippleController {

    private final RippleService rippleService;

    @GetMapping("/solution/{solutionId}")
    @Operation(summary = "Get ripple effect for a specific solution")
    public ResponseEntity<RippleDTO> getRippleForSolution(@PathVariable String solutionId) {
        RippleDTO ripple = rippleService.getRippleForSolution(solutionId);
        return ResponseEntity.ok(ripple);
    }

    @GetMapping("/pledge/{pledgeId}")
    @Operation(summary = "Get ripple effect for a user's pledge")
    public ResponseEntity<RippleDTO> getRippleForPledge(@PathVariable String pledgeId) {
        RippleDTO ripple = rippleService.getRippleForUserPledge(pledgeId);
        return ResponseEntity.ok(ripple);
    }
}